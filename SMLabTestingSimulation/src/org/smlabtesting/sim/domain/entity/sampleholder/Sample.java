package org.smlabtesting.sim.domain.entity.sampleholder;

import java.util.LinkedList;
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
    
    // Constructs
    public Sample() {}    
    
    // Entity API
    
    @Override
    public String getGlance() {
        return String.format(
                "[Sample]"
        );
    }
    
    List <Integer>  testSequence = new LinkedList<>();
    
    public boolean hasMatchingTestSequence(int i)
    {
        return testSequence.get(0) == i;
    }
    
    public boolean hasCompletedSequence(){
        return testSequence.size()==0;
    }
    
    
}
