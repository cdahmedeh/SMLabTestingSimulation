package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.BELT_SLOTS_COUNT;

/**
 * Maps to R.Racetrack
 */
public class RQRacetrack {
	// Attributes
	public int offset = 0;
    public int n() {return slots.length;}
	
    // Containers (Using Arrays.asList(T[]) to keep fixed size.
    public final RSampleHolder[] slots = new RSampleHolder[BELT_SLOTS_COUNT];
}
