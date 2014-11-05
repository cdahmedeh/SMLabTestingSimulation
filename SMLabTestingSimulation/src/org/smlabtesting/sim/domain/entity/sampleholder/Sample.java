package org.smlabtesting.sim.domain.entity.sampleholder;

import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to iC.Sample
 *
 * @author Ahmed El-Hajjar
 */
public class Sample extends Entity {
    // States
    protected enum SampleState implements State {
        Default;
    }

    // Factories
    public static Sample generateSample() {
        return new Sample();
    }
    
    // Constructs
    public Sample() {}    
    
    // Entity API
    
    @Override
    public String getGlance() {
        return String.format(
                "[Sample]"
        );
    }
}
