package org.smlabtesting.sim.domain.entity.sampleholder;

import static org.smlabtesting.sim.executor.Simulation.DEFAULT_RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.executor.Simulation;

/**
 * Maps to iC.Sample
 *
 * @author Ahmed El-Hajjar
 */
public class Sample extends Entity {
    // Constants
    private static final int[] SEQUENCE_ID = {0,1,2,3,4,5,6,7,8};
    private static final double[] PROBABILITIES = {0.09, 0.13, 0.15, 0.12, 0.07, 0.11, 0.14, 0.06, 0.13}; 
    
    private static final Integer[][] SEQUENCES = {
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
    public static Sample generateSample() {
        Sample sample = new Sample();
        sample.testSequence.addAll(Arrays.asList(SEQUENCES[distribution.sample()])); // TODO: Needs to be reversed
        return sample;
    }

    // Attributes
    List<Integer> testSequence = new ArrayList<>();
    
    // Constructs
    public Sample() {
    }    
    
    // Entity API
    
    @Override
    public String getGlance() {
        return String.format(
                "[Sample]"
        );
    }
    
    /**
     * Checks if the next test to be performed to for this sample corresponds
     * to the provided station number.
     * 
     * @return true if the next test does correspond. false otherwise, or if
     *         there are no more tests to perform
     */
    public boolean hasNextTest(int stationId) {
        return !hasCompletedSequence() && testSequence.get(0) == stationId;
    }

    /**
     * Checks if the sample has gone through all required tests.
     * 
     * @return true if the are no more tests to be done.
     */
    public boolean hasCompletedSequence() {
        return testSequence.isEmpty();
    }
    
    
}
