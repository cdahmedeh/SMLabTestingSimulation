package simModel.activity;

import simModel.SMLabModel;
import simModel.entity.ICSample;
import absmodJ.ConditionalActivity;

public class LoadUnloadProcessing extends ConditionalActivity {

	private SMLabModel model;

	public LoadUnloadProcessing(SMLabModel model) {
		this.model = model;
	}
	
	public static boolean precondition(SMLabModel model) {
		// TODO: The busy wasn't needed for our version of the system.
		return model.qUnloadBuffer.hasNext() && model.rcLoadUnloadMachine.busy == false;
	}

	@Override
	public void startingEvent() {
		// TODO: The busy wasn't needed for our version of the system.
		model.rcLoadUnloadMachine.busy = true;
		
        model.rcLoadUnloadMachine.icSampleHolder = model.qUnloadBuffer.removeQue();
        
        // TODO: May have been forgetten....
        if (!model.rcLoadUnloadMachine.icSampleHolder.hasSample()) {
        	model.qUnloadBuffer.emptySampleHolderCount--;
        }
	}

	@Override
	public double duration() {
        // Simulate cycle time.
        return model.rvp.generateCycleTime();
	}
	
	@Override
	protected void terminatingEvent() {
		// If icSample holder has a icSample, remove it.
        if (model.rcLoadUnloadMachine.icSampleHolder.hasSample()) {
            ICSample removedSample = model.rcLoadUnloadMachine.icSampleHolder.removeSample();
//            simulation.removeEntity(removedSample); //TODO: Is there an equivalent in ABSmodJ
        }

        // If a new icSamples is in line to be processed, insert into holder.
        if (model.qNewSamples.hasNext()) {
            model.rcLoadUnloadMachine.icSampleHolder.putSample(model.qNewSamples.removeQue());
        }
        
        // Queue to return to rqRacetrack.
        model.qRacetrackLine[0].insertQue(model.rcLoadUnloadMachine.icSampleHolder);
        model.rcLoadUnloadMachine.icSampleHolder = null;
        
     // TODO: The busy wasn't needed for our version of the system.
        model.rcLoadUnloadMachine.busy = false;

	}	
}
