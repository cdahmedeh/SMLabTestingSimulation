package org.smlabtesting.simabs.activity;

import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ScheduledActivity;

public class RacetrackMove extends ScheduledActivity {

	private SMLabModel model;

	public RacetrackMove(SMLabModel model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		// TODO Should be every second
		// TOOD Using 1 seems WRONG. Not triggered again, only every two.
		// TODO Get clock is a bad idea, plus it's a double, plus it was protected.
		return model.getClock() + 1;
	}

	@Override
	public void startingEvent() {
        // Move the belt one slot forward at every second.
        model.rqRacetrack.icSampleHolders.offset(1);

	}

	@Override
	protected double duration() {
		// TODO: In this system, it should be one, and not 0 unlike ours.
		// (Maybe ours does not handle lengths correctly)
		return 0;
	}
	
	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub

	}

}
