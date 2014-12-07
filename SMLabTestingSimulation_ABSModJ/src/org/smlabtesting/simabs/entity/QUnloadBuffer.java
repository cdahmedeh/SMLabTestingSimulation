package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.UNLOADBUFFER_CAPACITY;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.UnloadBuffer
 */
public class QUnloadBuffer {
    // Containers
    private final Deque<Integer> sampleHolderIds = new ArrayDeque<Integer>(UNLOADBUFFER_CAPACITY);

    // Attributes
    public int nEmpty = 0;
    public int n() {return sampleHolderIds.size();}
    
    // Methods for dealing with queues. In the CM, the methods are using the
    // global SP identifier.
    public Integer removeQue() {
        return sampleHolderIds.pop();
    }

    public void insertQue(final Integer entity) {
        sampleHolderIds.add(entity);
    }
}
