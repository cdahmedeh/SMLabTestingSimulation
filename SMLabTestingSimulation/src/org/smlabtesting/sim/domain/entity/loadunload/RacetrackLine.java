package org.smlabtesting.sim.domain.entity.loadunload;

import static org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine.RacetrackLineState.Default;

import java.util.ArrayDeque;
import java.util.Deque;

import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Queue;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to Q.Racetrack
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class RacetrackLine extends Entity implements Queue<SampleHolder> {
    // States
    protected enum RacetrackLineState implements State {
        Default;
    }

    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>();
    
    // Relationships
    private Racetrack racetrack;

    // Constructs
    public RacetrackLine(Racetrack racetrack) {
        this.racetrack = racetrack;
    }

    // Entity API
    @Override
    public void process() {
        // Initial State
        if (noState()) {
            setState(Default);
        }
        
        // If there is a holder waiting to enter and it's possible to merge
        // on the racetrack, then move the holder onto the racetrack. 
        if (racetrack.isVacant(Racetrack.LOAD_UNLOAD_EXIT)) {
            if (this.hasNext()) {
                racetrack.setSlot(Racetrack.LOAD_UNLOAD_EXIT, this.next());                
            }
        }
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

    @Override
    public boolean hasVacancy() {
        return true; // Unlimited size.
    }
}
