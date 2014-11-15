package org.smlabtesting.sim.domain.entity.testing;

import static org.smlabtesting.sim.domain.entity.testing.TestCellBuffer.UnloadBufferState.EnterUnloadBuffer;

import java.util.ArrayDeque;
import java.util.Deque;

import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.Queue;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to Q.TestCellBuffer
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class TestCellBuffer extends Entity implements Queue<SampleHolder> {
    // Constants
    private static final int BUFFER_SLOTS = 3; 

    // States
    protected enum UnloadBufferState implements State {
        EnterUnloadBuffer;
    }

    // Identifiers
    private final int stationId;
    
    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>(BUFFER_SLOTS);

    // Relationships
    private final Racetrack racetrack;

    // Constructs
    public TestCellBuffer(final Racetrack racetrack, final int stationId) {
        this.racetrack = racetrack;
        this.stationId = stationId;
    }

    // Entity API
    @Override
    public Handler[] generateHandlers() {
        return new Handler[] {
            new Handler(EnterUnloadBuffer) {
                @Override
                public boolean condition() {
                    SampleHolder holder = racetrack.peek(Racetrack.STATION_ENTRACES[stationId]);
                    return hasVacancy()
                            && holder != null
                            && holder.hasSample()
                            && holder.getSample().hasNextTest(stationId);
                }
                
                @Override
                public void begin() {
                    //Then move the holder onto the buffer. 
                    SampleHolder sampleHolder = racetrack.take(Racetrack.STATION_ENTRACES[stationId]);
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
