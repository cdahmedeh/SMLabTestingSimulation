package org.smlabtesting.sim.domain.entity.sampleholder;

/*
 * TestNode contains the TestSequence Number, and the retryCount
 * 
 * */
public class TestNode 
{
    private int retryCount;
    private Integer testNumber;
    
    
    public Integer getTestNumber() {
        return testNumber;
    }
    public void setTestNumber(Integer testNumber) {
        this.testNumber = testNumber;
    }
    public int getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    
}
