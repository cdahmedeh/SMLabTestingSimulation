package org.smlabtesting.simabs.entity;

import org.smlabtesting.simabs.types.OffsetList;

/**
 * Maps to R.Racetrack
 *
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class RQRacetrack {
    // Constants
    public static final int BELT_SLOTS_COUNT = 48;
    
    // Enter/Exit points for the load/unload machines and test cells.
    public static final int[] STATION_EXITS = {0, 8, 16, 24, 32, 40};
    public static final int[] STATION_ENTRANCES = {4, 12, 20, 28, 36, 44};
    
    // Containers
    public final OffsetList<ICSampleHolder> sampleHolders = new OffsetList<>(BELT_SLOTS_COUNT);

    // Public methods
    
    /**
     * Puts the provided icSample holder into the the specified position. Make
     * sure that there is room for it first with isVacant(..).
     *
     * @param position Zero-based index of where to put the holder in.
     * @param icSampleHolder The holder to slot into the track.
     */
    public void setSlot(final int position, final ICSampleHolder icSampleHolder) {
        sampleHolders.set(position, icSampleHolder);
    }
   
    /**
     * Gets a reference to the icSample holder in that position on the rqRacetrack
     * without actually removing it from there.
     *
     * @param position Zero-based index of where to get the holder from.
     * @param sampleHolder The holder at that postiion of the track.
     */
    public ICSampleHolder slots(final int position) {
        return sampleHolders.get(position);
    }

    /**
     * TODO: Write comments here.
     * @param i
     */
	public void shiftRacetrack(int i) {
		sampleHolders.offset(i);
	}
}
