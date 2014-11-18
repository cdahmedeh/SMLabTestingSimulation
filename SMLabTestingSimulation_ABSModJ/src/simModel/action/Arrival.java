package simModel.action;

import simModel.ModelName;
import simModel.entity.Sample;
import absmodJ.ScheduledAction;

public class Arrival extends ScheduledAction {

	private ModelName model;

	public Arrival(ModelName model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		//TODO: Is the next time, or the current time + next time?
        return nextArrival();
	}

	@Override
	protected void actionEvent() {
        // Create a sample.
        Sample sample = Sample.generateSample();
        model.newSamples.samples.add(sample);
//        simulation.addEntity(sample);

	}
}
