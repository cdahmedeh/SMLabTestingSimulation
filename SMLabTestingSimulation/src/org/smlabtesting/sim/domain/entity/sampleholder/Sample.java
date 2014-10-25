package org.smlabtesting.sim.domain.entity.sampleholder;

import static org.smlabtesting.sim.domain.entity.sampleholder.Sample.SampleState.Default;

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
    public void process() {
        // Initial state
        if (noState()) {
            setState(Default);
        }
    }

    @Override
    public String getGlance() {
        return String.format(
                "[Sample]"
        );
    }
}
