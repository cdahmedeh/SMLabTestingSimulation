package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.BELT_SLOTS_COUNT;

import org.smlabtesting.simabs.types.OffsetList;

/**
 * Maps to R.Racetrack
 */
public class RQRacetrack {
    // Containers
    public final OffsetList<Integer> slots = new OffsetList<>(BELT_SLOTS_COUNT);

    // Public methods
    
    /**
     * Puts the provided icSample holder into the the specified position. Make
     * sure that there is room for it first by check if the slot is null.
     *
     * @param position Zero-based index of where to put the holder in.
     * @param sampleHolderId The holderID corresponding to the sample holder to slot into the track.
     */
    public void setSlot(final int position, final Integer sampleHolderId) {
        slots.set(position, sampleHolderId);
    }
   
    /**
     * Gets a reference to the icSample holder in that position on the rqRacetrack
     * without actually removing it from there.
     *
     * @param position Zero-based index of where to get the holder from.
     * @param sampleHolder The holder at that position of the track.
     */
    public Integer slots(final int position) {
        return slots.get(position);
    }

}
