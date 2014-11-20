package simModel.entity;

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
	// TODO: BEGIN SHOULD MOVE
    // Constants
	public static final RandomGenerator DEFAULT_RNG = new Well19937a();
    private static final int[] SEQUENCE_ID = {0,1,2,3,4,5,6,7,8};
    private static final double[] PROBABILITIES = {0.09, 0.13, 0.15, 0.12, 0.07, 0.11, 0.14, 0.06, 0.13}; 
    
    private static final int[][] SEQUENCES = {
        { 1, 2, 4, 5 },
        { 3, 4, 5 },
        { 1, 2, 3, 4 },
        { 4, 3, 2 },
        { 2, 5, 1 },
        { 4, 5, 2, 3 },
        { 1, 5, 3, 4 },
        { 5, 3, 1 },
        { 2, 4, 5 }
    };
    
    // RNGs
    private static final IntegerDistribution distribution = new EnumeratedIntegerDistribution(DEFAULT_RNG, SEQUENCE_ID, PROBABILITIES);
	
    // Factories
    public static ICSample generateSample() {
        ICSample icSample = new ICSample();
        IntStream.of(SEQUENCES[distribution.sample()]).forEach(icSample.testSequence::add);
        return icSample;
    }
    
    // TODO: END SHOULD MOVE

    // Attributes
    Deque<Integer> testSequence = new ArrayDeque<>();
    
    // Entity API
    
    /**
     * Checks if the next test to be performed to for this icSample corresponds
     * to the provided station number.
     * 
     * @return true if the next test does correspond. false otherwise, or if
     *         there are no more tests to perform
     */
    public boolean hasNextTest(int stationId) {
        return !hasCompletedSequence() && testSequence.peek() == stationId;
    }

    /**
     * Checks if the icSample has gone through all required tests.
     * 
     * @return true if the are no more tests to be done.
     */
    public boolean hasCompletedSequence() {
        return testSequence.isEmpty();
    }
    
    /**
     * Marks that the upcoming has been completed. Called by the testing machine
     * when it finishes testing this icSample.
     */
    public void completedNextTest() {
        testSequence.pop();
    }
    
}
