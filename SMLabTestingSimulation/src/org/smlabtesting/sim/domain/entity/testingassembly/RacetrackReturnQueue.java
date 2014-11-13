package org.smlabtesting.sim.domain.entity.testingassembly;

import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;

public class RacetrackReturnQueue extends BoundedQueue<SampleHolder> 
{
    private static final int  boundedQueueSize=100;
    
    public RacetrackReturnQueue() 
    {
        super(boundedQueueSize);
    }
}
