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
		return model.unloadBuffer.hasNext();
	}

	@Override
	public void startingEvent() {
        model.loadUnloadMachine.sampleHolder = model.unloadBuffer.next();
	}

	@Override
	public double duration() {
        // Simulate cycle time.
        return generateCycleTime();
	}
	
	@Override
	protected void terminatingEvent() {
		// If sample holder has a sample, remove it.
        if (model.loadUnloadMachine.sampleHolder.hasSample()) {
            Sample removedSample = model.loadUnloadMachine.sampleHolder.removeSample();
            simulation.removeEntity(removedSample);
        }

        // If a new samples is in line to be processed, insert into holder.
        if (model.newSamples.hasNext()) {
            model.loadUnloadMachine.sampleHolder.putSample(model.newSamples.next());
        }
        
        // Queue to return to racetrack.
        model.racetrackLine[0].queue(model.loadUnloadMachine.sampleHolder);
        model.loadUnloadMachine.sampleHolder = null;

	}	
}
