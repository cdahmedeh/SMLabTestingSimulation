package org.smlabtesting.sim.domain.entity.testingassembly;

import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;

public class TestCellBuffer  extends BoundedQueue<SampleHolder>
{

    private static final  int boundedSize = 3; // length of the testcell
    public TestCellBuffer()
    {
        super(boundedSize);
        
    }
    
    
}
