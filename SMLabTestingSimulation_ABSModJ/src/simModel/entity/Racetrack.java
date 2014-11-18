package simModel.entity;

import static org.smlabtesting.sim.domain.entity.racetrack.Racetrack.RacetrackState.RacetrackMove;

import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.State;
import org.smlabtesting.types.OffsetList;

/**
 * Maps to R.Racetrack
 *
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class Racetrack {
    // Constants
    public static final int BELT_SLOTS_COUNT = 48;
    
    // Enter/Exit points for the load/unload machines and test cells.
    public static final int[] STATION_EXITS = {0, 8, 16, 24, 32, 40};
    public static final int[] STATION_ENTRANCES = {4, 12, 20, 28, 36, 44};
    
    // States
    protected enum RacetrackState implements State {
        RacetrackMove;
    }

    // Containers
    public final OffsetList<SampleHolder> sampleHolders = new OffsetList<>(BELT_SLOTS_COUNT);


        
    @Override
    public String getGlance() {
        return String.format(
                "[Racetrack] Sample Holders: %d - Offset Position: %d", 
                sampleHolders.count(),
                sampleHolders.getOffset()
        );
    }

    // Public methods
    
    /**
     * Puts the provided sample holder into the the specified position. Make
     * sure that there is room for it first with isVacant(..).
     *
     * @param position Zero-based index of where to put the holder in.
     * @param sampleHolder The holder to slot into the track.
     */
    public void setSlot(final int position, final SampleHolder sampleHolder) {
        sampleHolders.set(position, sampleHolder);
    }
   
    /**
     * Checks if the specific position has room for a sample holder and not 
     * taken by any other.
     * 
     * @param position Zero-based index of where to verify vacancy.
     * @return True if a holder can be accodomated at that position.
     */
    public boolean isVacant(final int position) {
        return sampleHolders.get(position) == null;
    }
   
    /**
     * Takes the sample holder at the provided position out of the race track 
     * and returns its reference.
     *
     * @param position Zero-based index of where to get the holder from.
     * @param sampleHolder The holder that was removed from the track.
     */
    public SampleHolder take(final int position) {
        SampleHolder sampleHolder = sampleHolders.get(position);
        sampleHolders.set(position, null);
        return sampleHolder;
    }
    
    /**
     * Checks if the specific position a sample holder already in it. 
     * 
     * @param position Zero-based index of position to verify.
     * @return True if a holder is already here.
     */
    public boolean isTaken(final int position) {
        return !isVacant(position);
    }
   
    /**
     * Gets a reference to the sample holder in that position on the racetrack
     * without actually removing it from there.
     *
     * @param position Zero-based index of where to get the holder from.
     * @param sampleHolder The holder at that postiion of the track.
     */
    public SampleHolder peek(final int position) {
        return sampleHolders.get(position);
    }
}
