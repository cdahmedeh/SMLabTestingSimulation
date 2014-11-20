package org.smlabtesting.simabs.activity;

import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ScheduledActivity;

/**
 * This action describes the movement of the track as the holders get shifted 
 * forward in a counter-clockwise fashion. Everything is shifted by one slot,
 * every second.
 * 
 * Instead of having a duration 1 second event, and causing confusion for the 
 * occurrence times of this activity, the duration is made instant. Otherwise, 
 * depend on the timing algorithm of the simulation, the racetrack may be moved
 * every two seconds if an event is not immediately re-scheduled, conditions 
 * re-evaluated, and then re-run again.
 * 
 * Also, this condition is never ever false, and this can cause some 
 * interesting issues in the ABCmod simulation algorithms. Thus, when doing a 
 * time advance, this condition will get re-tested again, and again, and never
 * ever become false, thus generating an infinite loop.
 * 
 * In addition, using a time sequence instead of a conditional event with
 * duration means that the other activities don�t have to check that the
 * racetrack is moving.
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
		return model.getClock() + 1;
	}

	@Override
	public void startingEvent() {
        // Move the belt one slot forward.
        model.rqRacetrack.shiftRacetrack(1);
	}

	@Override
	protected double duration() {
		return 0;
	}
	
	@Override
	protected void terminatingEvent() {
		
	}

}
