package org.smlabtesting.simabs.activity;

import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalActivity;

/**
 * This activity represents the Load/Unload Machine performing the required 
 * operations to process Sample Holders and Samples as described in the
 * Load/unloading machine behaviors section.
 * 
 * Participants: R.LoadUnloadMachine
 * Uses: Q.NewSamples, Q.UnloadBuffer, Q.RacetrackLine, 
 *       R.SampleHolder (implicit), iC.Sample (implicit)
 */
public class LoadUnloadProcessing extends ConditionalActivity {
	private SMLabModel model;

	public LoadUnloadProcessing(SMLabModel model) {
		this.model = model;
	}
	
	public static boolean precondition(SMLabModel model) {
		// TODO: The busy attribute wasn't needed for our version of the system.
		
		// The unload buffer has a holder waiting in line and the load unload 
		// machine is not currently processing another holder
		return model.qUnloadBuffer.n() > 0 && model.rLoadUnloadMachine.busy == false;
	}

	@Override
	public void startingEvent() {
		// The machine status is set to busy.
		model.rLoadUnloadMachine.busy = true;
		
		// Take the next sample holder in line from the unload buffer and load
		// it in the machine
        model.rLoadUnloadMachine.sampleHolder = model.qUnloadBuffer.removeQue();
        
        // If the holder was an empty,  decrement the empty holder counter as 
        // it will be removed.
        if (model.rLoadUnloadMachine.sampleHolder.sample == null) {
        	model.qUnloadBuffer.nEmpty--;
        }
	}

	@Override
	public double duration() {
        // Simulate cycle time. It randomly varies according to the 
		// distribution in the RVP.
        return model.rvp.uLoadUnloadMachineCycleTime();
	}
	
	@Override
	protected void terminatingEvent() {
		// If the holder in the machine has a sample, remove it from the 
		// system.
        if (model.rLoadUnloadMachine.sampleHolder != null) {
        	// No Behavior belongs to ICSample, so these are implicit.        	
            // ICSample icSample = model.rcLoadUnloadMachine.sampleHolder.sample;
            // SP.Leave(icSample)
            model.rLoadUnloadMachine.sampleHolder.sample = null;
        }

        // If there is a new sample waiting to be tested, put it in the holder.
        if (model.qNewSamples.n() > 0) {
        	// TODO: This queue will give priority to rush samples
            model.rLoadUnloadMachine.sampleHolder.sample = model.qNewSamples.removeQue();
        }
        
        // Put the holder in line to return to the racetrack.
        model.qRacetrackLine[0].insertQue(model.rLoadUnloadMachine.sampleHolder);
        model.rLoadUnloadMachine.sampleHolder = null;
        
        // The machine status is set to idle.
        model.rLoadUnloadMachine.busy = false;

	}	
}
