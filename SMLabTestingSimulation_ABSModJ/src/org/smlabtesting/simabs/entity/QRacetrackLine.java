package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.Racetrack
 */
public class QRacetrackLine {

    // Containers
    private final Deque<ICSampleHolder> sampleHolders = new ArrayDeque<ICSampleHolder>();
    
    // Attributes
    public int n() {return sampleHolders.size();}

    // Methods for dealing with queues. In the CM, the methods are using the
    // global SP identifier.
    public ICSampleHolder removeQue() {
        return sampleHolders.pop();
    }

    public void insertQue(final ICSampleHolder entity) {
        sampleHolders.add(entity);
    }
}
