package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.variable.Constants.UNLOADBUFFER_CAPACITY;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.UnloadBuffer
 */
public class QUnloadBuffer {
    public int maxEmptyHolders = 3; //TODO: Make me a parameter.

    // Containers
    private final Deque<ICSampleHolder> icSampleHolders = new ArrayDeque<ICSampleHolder>(UNLOADBUFFER_CAPACITY);

    // Attributes
    public int nEmpty = 0;
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
