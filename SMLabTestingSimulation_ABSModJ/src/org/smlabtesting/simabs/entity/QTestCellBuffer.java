package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.TestCellBuffer
 */
public class QTestCellBuffer {
    // Containers
    private final Deque<Integer> sampleHoldersId = new ArrayDeque<Integer>(TEST_CELL_BUFFER_CAPACITY);
    
    // Attributes
    public int n() {return sampleHoldersId.size();}
    
    // Methods for dealing with queues. In the CM, the methods are using the
    // global SP identifier.
    public Integer removeQue() {
        return sampleHoldersId.pop();
    }

    public void insertQue(final Integer sampleHolderId) {
        sampleHoldersId.add(sampleHolderId);
    }
}
