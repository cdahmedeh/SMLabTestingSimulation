package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.Racetrack
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 *
 */
public class QRacetrackLine {

    // Containers
    private final Deque<ICSampleHolder> icSampleHolders = new ArrayDeque<ICSampleHolder>();
    
    // Queue API
    public int n() {
    	return icSampleHolders.size();
    }

    public ICSampleHolder removeQue() {
        return icSampleHolders.pop();
    }

    public void insertQue(final ICSampleHolder entity) {
        icSampleHolders.add(entity);
    }
}
