package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to iC.Sample
 *
 * @author Ahmed El-Hajjar
 */
public class ICSample {
    // Attributes
    public Deque<Integer> testsRemaining = new ArrayDeque<>();
    
    // Entity API
    
    /**
     * Checks if the next test to be performed to for this icSample corresponds
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
     * when it finishes testing this icSample.
     */
    public void completedNextTest() {
        testsRemaining.pop();
    }
    
}
