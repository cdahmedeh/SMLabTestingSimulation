package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.NewSamples.
 */
public class QNewSamples {
	// Types of samples.
	public static int REGULAR = 0;
	public static int RUSH = 1;
		
    // Containers
    public final Deque<ICSample> samples = new ArrayDeque<ICSample>();
	

    // Attributes
    public int n() {return samples.size();}
    
    // Methods for dealing with queues. In the CM, the methods are using the
    // global SP identifier.
    public ICSample removeQue() {
        return samples.pop();
    }

    public void insertQue(final ICSample entity) {
        samples.add(entity);
    }
}
