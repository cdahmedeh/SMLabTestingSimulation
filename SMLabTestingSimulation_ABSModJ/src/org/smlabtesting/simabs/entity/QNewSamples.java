package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.NewSamples.
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class QNewSamples {
    // Containers
    public final Deque<ICSample> samples = new ArrayDeque<ICSample>();

    // Queue API
    public ICSample removeQue() {
        return samples.pop();
    }

    public void insertQue(final ICSample entity) {
        samples.add(entity);
    }
    
    public int n() {
    	return samples.size();
    }
}
