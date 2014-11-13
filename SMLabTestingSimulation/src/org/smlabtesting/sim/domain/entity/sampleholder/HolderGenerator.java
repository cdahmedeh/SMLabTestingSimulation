package org.smlabtesting.sim.domain.entity.sampleholder;

import javax.xml.ws.Holder;

public class HolderGenerator {

    private static final HolderGenerator instance = new HolderGenerator();
    
    private HolderGenerator(){}
    
    public HolderGenerator getInstance()
    {
        return instance;
    }
    
    public static int totalSampleHolderCount;
    private static int thresholdCount = 70;
    
    public static SampleHolder   generateSampleHolder()
    {
        if(totalSampleHolderCount >= thresholdCount)
             return null;
            ///throw new RuntimeException("Total number of sampleHolderCount is greater than threshold");
        
            totalSampleHolderCount++;
            return new SampleHolder();
    }
    
    public void decreaseTotalHolderCount()
    {
        totalSampleHolderCount--;
    }
}
