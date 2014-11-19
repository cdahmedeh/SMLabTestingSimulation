package simModel.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.TestCellBuffer
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class TestCellBuffer {
    // Constants
    private static final int BUFFER_SLOTS = 3; 

    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>(BUFFER_SLOTS);
    
    // Queue API
    public boolean hasNext() {
        return sampleHolders.peek() != null;
    }

    public SampleHolder next() {
        return sampleHolders.pop();
    }

    public void queue(final SampleHolder entity) {
        sampleHolders.add(entity);
    }

    public boolean hasVacancy() {
        return sampleHolders.size() < BUFFER_SLOTS;
    }
}
