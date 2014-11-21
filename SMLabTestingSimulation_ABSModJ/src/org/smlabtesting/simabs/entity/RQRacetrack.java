package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.BELT_SLOTS_COUNT;

import org.smlabtesting.simabs.types.OffsetList;

/**
 * Maps to R.Racetrack
 */
public class RQRacetrack {
    // Containers
    public final OffsetList<RSampleHolder> sampleHolders = new OffsetList<>(BELT_SLOTS_COUNT);

    // Public methods
    
    /**
     * Puts the provided icSample holder into the the specified position. Make
     * sure that there is room for it first by check if the slot is null.
     *
     * @param position Zero-based index of where to put the holder in.
     * @param sampleHolder The holder to slot into the track.
     */
    public void setSlot(final int position, final RSampleHolder sampleHolder) {
        sampleHolders.set(position, sampleHolder);
    }
   
    /**
     * Gets a reference to the icSample holder in that position on the rqRacetrack
     * without actually removing it from there.
     *
     * @param position Zero-based index of where to get the holder from.
     * @param sampleHolder The holder at that postiion of the track.
     */
    public RSampleHolder slots(final int position) {
        return sampleHolders.get(position);
    }

    /**
     * Shifts the track by n spaces forward (clockwise). This is based on the
     * UP.shiftRacetrack() user-defined procedure in the CM. See OffsetList
     * type for optimization trick.
     *  
     * @param n The number of positions to shift by. Positives values indicate
     * 			clockwise movement while negative counter-clockwise. 
     */
	public void shiftRacetrack(int n) {
		sampleHolders.offset(n);
	}
}
