package org.smlabtesting.sim.domain.entity;

import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.State;

/**
 * A sample holder is what holds the samples and it is used for transforming
 * and manipulating them throughout most of the system.
 *  
 * @author Ahmed El-Hajjar
 */
public class SampleHolder extends Entity {
	protected enum SampleHolderState implements State {
		EnteringRaceTrack,
		TraversingRaceTrack,
		ExitRaceTrack
	}
	
	// Relationships
	public Sample sample;
	
	// Entity API
	@Override
	public void process() {
		//TODO: Implement me.
	}
}
