package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.Racetrack
 */
public class QRacetrackLine {

    // Containers
    private final Deque<Integer> sampleHolderIds = new ArrayDeque<Integer>();
    
    // Attributes
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
