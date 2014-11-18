package simModel.entity;

import static org.smlabtesting.sim.domain.entity.racetrack.RacetrackLine.RacetrackLineState.ExitRacetrackLine;

import java.util.ArrayDeque;
import java.util.Deque;

import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.Queue;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to Q.Racetrack
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 *
 */
public class RacetrackLine implements Queue<SampleHolder> {

    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>();
    
    // Relationships
    private Racetrack racetrack;

    // Identifiers
    private int stationId;

    // Constructs
    public RacetrackLine(int stationId) {
        this.stationId = stationId;
    }


    
    @Override
    public String getGlance() {
        return String.format(
                "[RacetrackLine] Sample Holders waiting to enter racetrack: %d", 
                sampleHolders.size()
        );
    }
    
    // Queue API
    @Override
    public boolean hasNext() {
        return sampleHolders.peek() != null;
    }

    @Override
    public SampleHolder next() {
        return sampleHolders.pop();
    }

    @Override
    public void queue(final SampleHolder entity) {
        sampleHolders.add(entity);
    }
}
