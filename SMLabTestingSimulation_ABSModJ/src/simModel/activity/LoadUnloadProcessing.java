package simModel.activity;

import simModel.ModelName;
import simModel.entity.Sample;
import absmodJ.ConditionalActivity;

public class LoadUnloadProcessing extends ConditionalActivity {

	private ModelName model;

	public LoadUnloadProcessing(ModelName model) {
		this.model = model;
	}
	
	public static boolean precondition(ModelName model) {
		// TODO: The busy wasn't needed for our version of the system.
		return model.unloadBuffer.hasNext() && model.loadUnloadMachine.busy == false;
	}

	@Override
	public void startingEvent() {
		// TODO: The busy wasn't needed for our version of the system.
		model.loadUnloadMachine.busy = true;
		
        model.loadUnloadMachine.sampleHolder = model.unloadBuffer.removeQue();
        
        // TODO: May have been forgetten....
        if (!model.loadUnloadMachine.sampleHolder.hasSample()) {
        	model.unloadBuffer.emptySampleHolderCount--;
        }
	}

	@Override
	public double duration() {
        // Simulate cycle time.
        return model.rvp.generateCycleTime();
	}
	
	@Override
	protected void terminatingEvent() {
		// If sample holder has a sample, remove it.
        if (model.loadUnloadMachine.sampleHolder.hasSample()) {
            Sample removedSample = model.loadUnloadMachine.sampleHolder.removeSample();
//            simulation.removeEntity(removedSample); //TODO: Is there an equivalent in ABSmodJ
        }

        // If a new samples is in line to be processed, insert into holder.
        if (model.newSamples.hasNext()) {
            model.loadUnloadMachine.sampleHolder.putSample(model.newSamples.removeQue());
        }
        
        // Queue to return to racetrack.
        model.racetrackLine[0].insertQue(model.loadUnloadMachine.sampleHolder);
        model.loadUnloadMachine.sampleHolder = null;
        
     // TODO: The busy wasn't needed for our version of the system.
        model.loadUnloadMachine.busy = false;

	}	
}
