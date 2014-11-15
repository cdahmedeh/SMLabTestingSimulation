package org.smlabtesting.sim.domain.entity.sampleholder;

import java.util.ArrayList;
import java.util.List;

import org.smlabtesting.sim.domain.generic.Entity;

/**
 * Maps to iC.Sample
 *
 * @author Ahmed El-Hajjar
 */
public class Sample extends Entity {
    // Factories
    public static Sample generateSample() {
        return new Sample();
    }

    // Attributes
    List<Integer> testSequence = new ArrayList<>();
    
    // Constructs
    public Sample() {}    
    
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
