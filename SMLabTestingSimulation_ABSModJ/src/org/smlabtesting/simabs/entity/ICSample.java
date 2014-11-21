package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to iC.Sample
 */
public class ICSample {
    // Attributes (they are set by Arrival)
    public Deque<Integer> testsRemaining = new ArrayDeque<>();
    public double startTime = 0.0;
    public boolean rush = false;
    
    // Public Methods
    
    /**
     * Checks if the next test to be performed to for this sample corresponds
     * to the provided station number.
     * 
     * @return true if the next test does correspond. false otherwise, or if
     *         there are no more tests to perform
     */
    public boolean testsRemainingNext(int stationId) {
        return !testsRemaining.isEmpty() && testsRemaining.peek() == stationId;
    }
 
    /**
     * Marks that the upcoming has been completed. Called by the testing machine
     * when it finishes testing this sample.
     */
    public void completeNextTest() {
        testsRemaining.pop();
    }
}
