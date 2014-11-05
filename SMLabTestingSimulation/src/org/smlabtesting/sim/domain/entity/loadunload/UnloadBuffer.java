package org.smlabtesting.sim.domain.entity.loadunload;

import static org.smlabtesting.sim.domain.entity.loadunload.UnloadBuffer.UnloadBufferState.EnterUnloadBuffer;

import java.util.ArrayDeque;
import java.util.Deque;

import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.Queue;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to Q.UnloadBuffer
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class UnloadBuffer extends Entity implements Queue<SampleHolder> {
    // Constants
    private static final int BUFFER_SLOTS = 5; 

    // States
    protected enum UnloadBufferState implements State {
        EnterUnloadBuffer;
    }
    
    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>(BUFFER_SLOTS);

    // Relationships
    private Racetrack racetrack;

    // Constructs
    public UnloadBuffer(Racetrack racetrack) {
        this.racetrack = racetrack;
    }

    // Entity API
    @Override
    public Handler[] generateHandlers() {
        return new Handler[] {
            new Handler(EnterUnloadBuffer) {
                @Override
                public boolean condition() {
                    // TODO: This isn't part of the correct simulated behavior. For testing
                    // purposes only.
                    //
                    // As soon a sample holder get to the enty point of the load/unload
                    // machine, get it in. Just to prevent from the simulation from
                    // doing nothing. Not even the right place to do it.
                    return racetrack.isTaken(Racetrack.LOAD_UNLOAD_ENTRANCE) && hasVacancy();
                }
                
                @Override
                public void begin() {
                    //Then move the holder onto the racetrack. 
                    SampleHolder sampleHolder = racetrack.take(Racetrack.LOAD_UNLOAD_ENTRANCE);
                    queue(sampleHolder);
                }
            } 
        };
    };
    
    @Override
    public String getGlance() {
        return String.format(
                "[UnloadBuffer] Holders waiting for processing: %d", 
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
        return sampleHolders.size() < BUFFER_SLOTS;
    }
}
