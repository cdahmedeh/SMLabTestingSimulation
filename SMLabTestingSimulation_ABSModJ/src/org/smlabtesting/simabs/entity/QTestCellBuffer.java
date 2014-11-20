package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.TestCellBuffer
 */
public class QTestCellBuffer {
    // Containers
    private final Deque<ICSampleHolder> icSampleHolders = new ArrayDeque<ICSampleHolder>(TEST_CELL_BUFFER_CAPACITY);
    
    // Attributes
    public int n() {return icSampleHolders.size();}
    
    // Methods for dealing with queues. In the CM, the methods are using the
    // global SP identifier.
    public ICSampleHolder removeQue() {
        return icSampleHolders.pop();
    }

    public void insertQue(final ICSampleHolder entity) {
        icSampleHolders.add(entity);
    }
}
