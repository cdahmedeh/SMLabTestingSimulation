package simModel.activity;

import simModel.ModelName;
import absmodJ.ScheduledActivity;

public class RacetrackMove extends ScheduledActivity {

	private ModelName model;

	public RacetrackMove(ModelName model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		// TODO Should be every second
		// TODO Get clock is a bad idea, plus it's a double, plus it was protected.
		return model.getClock() + 1;
	}

	@Override
	public void startingEvent() {
        // Move the belt one slot forward at every second.
        model.racetrack.sampleHolders.offset(1);

	}

	@Override
	protected double duration() {
		// TODO: In this system, it should be one, and not 0 unlike ours.
		// (Maybe ours does not handle lengths correctly)
		return 1;
	}
	
	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub

	}

}
