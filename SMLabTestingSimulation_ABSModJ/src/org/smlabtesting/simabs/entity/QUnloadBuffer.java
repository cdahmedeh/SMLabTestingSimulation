package org.smlabtesting.simabs.entity;

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
    public static final int UNLOADBUFFER_CAPACITY = 5; 
    public int maxEmptyHolders = 3;

    // Containers
    private final Deque<ICSampleHolder> icSampleHolders = new ArrayDeque<ICSampleHolder>(UNLOADBUFFER_CAPACITY);

    // Attributes
    public int nEmpty = 0;
    
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
        return icSampleHolders.size() < UNLOADBUFFER_CAPACITY;
    }
    
    public int n() {
    	return icSampleHolders.size();
    }
}
