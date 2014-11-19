package simModel.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.UnloadBuffer
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 *
 */
public class UnloadBuffer {
    // Constants
    private static final int BUFFER_SLOTS = 5; 
    public static int MAX_EMPTY_HOLDERS = 3;

    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>(BUFFER_SLOTS);

    // Attributes
    public int emptySampleHolderCount = 0;
    
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
