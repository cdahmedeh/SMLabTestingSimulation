package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.Racetrack
 */
public class QRacetrackLine {

    // Containers
    private final Deque<RSampleHolder> sampleHolders = new ArrayDeque<RSampleHolder>();
    
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
