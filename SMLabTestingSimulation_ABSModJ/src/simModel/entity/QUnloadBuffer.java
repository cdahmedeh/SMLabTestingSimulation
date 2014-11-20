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
public class QUnloadBuffer {
    // Constants
    private static final int BUFFER_SLOTS = 5; 
    public static int MAX_EMPTY_HOLDERS = 3;

    // Containers
    private final Deque<ICSampleHolder> icSampleHolders = new ArrayDeque<ICSampleHolder>(BUFFER_SLOTS);

    // Attributes
    public int emptySampleHolderCount = 0;
    
    // Queue API
    public boolean hasNext() {
        return icSampleHolders.peek() != null;
    }

    public ICSampleHolder removeQue() {
        return icSampleHolders.pop();
    }

    public void insertQue(final ICSampleHolder entity) {
        icSampleHolders.add(entity);
    }

    public boolean hasVacancy() {
        return icSampleHolders.size() < BUFFER_SLOTS;
    }
}
