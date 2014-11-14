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
 * @author Ahmed El-Hajjar
 *
 */
public class TestCellBuffer extends Entity implements Queue<SampleHolder> {
    // Constants
    private static final int BUFFER_SLOTS = 3; 

    private int stationid;
    // States
    protected enum UnloadBufferState implements State {
        EnterUnloadBuffer;
    }
    
    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>(BUFFER_SLOTS);

    // Relationships
    private Racetrack racetrack;

    // Constructs
    public TestCellBuffer(Racetrack racetrack , int stationid) {
        this.racetrack = racetrack;
        this.stationid = stationid;
    }

    // Entity API
    @Override
    public Handler[] generateHandlers() {
        return new Handler[] {
            new Handler(EnterUnloadBuffer) {
                @Override
                public boolean condition() {
                    
                       int stationEntrace = Racetrack.STATION_ENTRACES[stationid];
                       SampleHolder holder  = racetrack.peek(stationEntrace);
                    return hasVacancy() && ( holder != null) && holder.hasSample() && holder.getSample().hasMatchingTestSequence(stationid);
                    
                            
                }
                
                @Override
                public void begin() {
                    //Then move the holder onto the racetrack. 
                    SampleHolder sampleHolder = racetrack.take(Racetrack.STATION_ENTRACES[0]);
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
