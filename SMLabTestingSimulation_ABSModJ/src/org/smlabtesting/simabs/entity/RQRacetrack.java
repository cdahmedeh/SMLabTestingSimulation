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
    public final OffsetList<ICSampleHolder> icSampleHolders = new OffsetList<>(BELT_SLOTS_COUNT);

    // Public methods
    
    /**
     * Puts the provided icSample holder into the the specified position. Make
     * sure that there is room for it first with isVacant(..).
     *
     * @param position Zero-based index of where to put the holder in.
     * @param icSampleHolder The holder to slot into the track.
     */
    public void setSlot(final int position, final ICSampleHolder icSampleHolder) {
        icSampleHolders.set(position, icSampleHolder);
    }
   
    /**
     * Checks if the specific position has room for a icSample holder and not 
     * taken by any other.
     * 
     * @param position Zero-based index of where to verify vacancy.
     * @return True if a holder can be accodomated at that position.
     */
    public boolean isVacant(final int position) {
        return icSampleHolders.get(position) == null;
    }
   
    /**
     * Takes the icSample holder at the provided position out of the race track 
     * and returns its reference.
     *
     * @param position Zero-based index of where to get the holder from.
     * @param icSampleHolder The holder that was removed from the track.
     */
    public ICSampleHolder take(final int position) {
        ICSampleHolder icSampleHolder = icSampleHolders.get(position);
        icSampleHolders.set(position, null);
        return icSampleHolder;
    }
    
    /**
     * Checks if the specific position a icSample holder already in it. 
     * 
     * @param position Zero-based index of position to verify.
     * @return True if a holder is already here.
     */
    public boolean isTaken(final int position) {
        return !isVacant(position);
    }
   
    /**
     * Gets a reference to the icSample holder in that position on the rqRacetrack
     * without actually removing it from there.
     *
     * @param position Zero-based index of where to get the holder from.
     * @param icSampleHolder The holder at that postiion of the track.
     */
    public ICSampleHolder slots(final int position) {
        return icSampleHolders.get(position);
    }
}
