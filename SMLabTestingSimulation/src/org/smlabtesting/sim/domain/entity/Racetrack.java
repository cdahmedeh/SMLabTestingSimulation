package org.smlabtesting.sim.domain.entity;

import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.State;
import org.smlabtesting.types.OffsetList;

/**
 * The racetrack is where sample holders are transported from one station to 
 * another.
 * 
 * @author Ahmed El-Hajjar
 */
public class Racetrack extends Entity {
	protected enum RacetrackState implements State {
		Moving
	}

	// Parameters
	public static final int BELT_SLOTS_COUNT = 48;
	
	// Relationships
	private OffsetList<SampleHolder> sampleHolders = new OffsetList<>(BELT_SLOTS_COUNT);

	/**
	 * Puts the provided sample holder into the the specified position.
	 * 
	 * TODO: Handle vacancy.
	 * 
	 * @param position Zero-based index of where to put the holder in.
	 * @param sampleHolder The holder to slot into the track.
	 */
	public void setSlot(int position, SampleHolder sampleHolder) {
		sampleHolders.set(position, sampleHolder);
	}
	
	// Entity API
	@Override
	public void process() {
		// Initial state
		if (state == null) {
			state = RacetrackState.Moving;
		}
		
		// Move the belt one slot forward.
		sampleHolders.offset(1);
	}

}
