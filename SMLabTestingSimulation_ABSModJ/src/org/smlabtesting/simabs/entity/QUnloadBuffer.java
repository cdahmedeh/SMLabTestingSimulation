package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.UNLOADBUFFER_CAPACITY;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.UnloadBuffer
 */
public class QUnloadBuffer {
    // Containers
    private final Deque<Integer> sampleHoldersId = new ArrayDeque<Integer>(UNLOADBUFFER_CAPACITY);

    // Attributes
    public int nEmpty = 0;
    public int n() {return sampleHoldersId.size();}
    
    // Methods for dealing with queues. In the CM, the methods are using the
    // global SP identifier.
    public Integer removeQue() {
        return sampleHoldersId.pop();
    }

    public void insertQue(final Integer entityId) {
        sampleHoldersId.add(entityId);
    }
}
