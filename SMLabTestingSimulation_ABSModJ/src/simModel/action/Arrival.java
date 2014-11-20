package simModel.action;

import simModel.SMLabModel;
import simModel.entity.ICSample;
import absmodJ.ScheduledAction;

public class Arrival extends ScheduledAction {

	private SMLabModel model;

	public Arrival(SMLabModel model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		//TODO: Is the next time, or the current time + next time?
        return model.getClock() + model.rvp.nextArrival();
	}

	@Override
	protected void actionEvent() {
        // Create a icSample.
        ICSample icSample = ICSample.generateSample();
        model.qNewSamples.icSamples.add(icSample);
//        simulation.addEntity(icSample);

	}
}
