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
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 *
 */
public class UnloadBuffer extends Entity implements Queue<SampleHolder> {
    // Constants
    private static final int BUFFER_SLOTS = 5; 
    private static int MAX_EMPTY_HOLDERS = 3;

    // States
    protected enum UnloadBufferState implements State {
        EnterUnloadBuffer;
    }
    
    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>(BUFFER_SLOTS);

    // Attributes
    private int emptySampleHolderCount = 0;
    
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
                    SampleHolder holder = racetrack.peek(Racetrack.STATION_ENTRACES[0]);
                    
                    return racetrack.isTaken(Racetrack.STATION_ENTRACES[0]) && hasVacancy() 
                            && (
                                    holder.hasSample()  && holder.getSample().hasCompletedSequence() || 
                                    !holder.hasSample() && emptySampleHolderCount < MAX_EMPTY_HOLDERS
                               );
                }
                
                @Override
                public void begin() {
                    //Then move the holder onto the racetrack. 
                    SampleHolder sampleHolder = racetrack.take(Racetrack.STATION_ENTRACES[0]);
                    
                    if(!sampleHolder.hasSample()) {
                        emptySampleHolderCount++;
                    }
                        
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
