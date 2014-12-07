package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.TestCellBuffer
 */
public class QTestCellBuffer {
    // Containers
    private final Deque<RSampleHolder> sampleHolders = new ArrayDeque<RSampleHolder>(TEST_CELL_BUFFER_CAPACITY);
    
    // Attributes
    public int n() {return sampleHolders.size();}
    
    // Methods for dealing with queues. In the CM, the methods are using the
    // global SP identifier.
    public RSampleHolder removeQue() {
        return sampleHolders.pop();
    }

    public void insertQue(final RSampleHolder entity) {
        sampleHolders.add(entity);
    }
}
