package org.smlabtesting.simabs.activity;

import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ScheduledActivity;

/**
 * This action describes the movement of the track as the holders get shifted 
 * forward in a counter-clockwise fashion. Everything is shifted by one slot, 
 * every second.
 * 
 * TODO: Mention optimizations.
 * TODO: Rant about duration.
 * TODO: Handle Racetrack.InMotion.
 * 
 * Participants: RQ.Racetrack
 */
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
        // Move the belt one slot forward.
        model.rqRacetrack.shiftRacetrack(1);
	}

	@Override
	protected double duration() {
		// TODO: In the CM, it says 0, but it should be 1. Otherwise, the
		// racetrack is shifted every 2 seconds.
		return 0;
	}
	
	@Override
	protected void terminatingEvent() {
		
	}

}
