package org.smlabtesting.sim.domain.entity.racetrack;

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
public class RacetrackLine extends Entity implements Queue<SampleHolder> {
    // States
    protected enum RacetrackLineState implements State {
        ExitRacetrackLine;
    }

    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>();
    
    // Relationships
    private Racetrack racetrack;

    // Identifiers
    private int stationId;

    // Constructs
    public RacetrackLine(Racetrack racetrack, int stationId) {
        this.racetrack = racetrack;
        this.stationId = stationId;
    }

    // Entity API
    @Override
    public Handler[] generateHandlers() {
        return new Handler[] {
            new Handler(ExitRacetrackLine) {
                @Override
                public boolean condition() {
                    // If there is a holder waiting to enter and it's possible to merge
                    // on the racetrack.
                    return hasNext() && racetrack.isVacant(Racetrack.STATION_EXITS[stationId]);
                }
                
                @Override
                public void begin() {
                    //Then move the holder onto the racetrack. 
                    racetrack.setSlot(Racetrack.STATION_EXITS[stationId], next());
                }
            } 
        };
    };
    
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
