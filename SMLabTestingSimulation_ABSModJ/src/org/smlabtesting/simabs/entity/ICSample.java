package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;

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
        return !hasCompletedSequence() && testsRemaining.peek() == stationId;
    }

    /**
     * Checks if the icSample has gone through all required tests.
     * 
     * @return true if the are no more tests to be done.
     */
    public boolean hasCompletedSequence() {
        return testsRemaining.isEmpty();
    }
    
    /**
     * Marks that the upcoming has been completed. Called by the testing machine
     * when it finishes testing this icSample.
     */
    public void completedNextTest() {
        testsRemaining.pop();
    }
    
}
