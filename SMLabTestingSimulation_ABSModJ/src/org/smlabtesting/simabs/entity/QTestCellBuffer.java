package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.TestCellBuffer
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class QTestCellBuffer {
    // Constants
    private static final int BUFFER_SLOTS = 3; 

    // Containers
    private final Deque<ICSampleHolder> icSampleHolders = new ArrayDeque<ICSampleHolder>(BUFFER_SLOTS);
    
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
