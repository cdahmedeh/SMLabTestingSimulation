package org.smlabtesting.sim.domain.entity.sampleholder;

import java.util.LinkedList;
import java.util.List;

import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
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
    public Handler[] generateHandlers() {
        return new Handler[]{};
    };

    @Override
    public String getGlance() {
        return String.format(
                "[Sample]"
        );
    }
    TestSequence testsRemaining ;
    
    // TestSequence is used for checking the testsequence associated with the sample
    // It has a retrycount for the testing done in the cell
    // each Sample has to have a testSequence
   
    public void setTestsRemaining(List <Integer> testRemaining) 
    {
        if(testRemaining == null || testRemaining.isEmpty())
            return;
        this.testsRemaining = new TestSequence(testRemaining);
    }

    private class TestSequence
    {
        private final  LinkedList <TestNode> list = new LinkedList<TestNode>();
    
        TestSequence()
        {
            
        }
        
        public TestNode getFirstTestSequence()
        {
            return list.getFirst();
            
        }
        
        
        public TestNode removeFirstTestSequence()
        {
           return list.removeFirst();
        }
        
        public void retryTest(int testNumber)
        {
            for(TestNode node: list)
            {
                if(node.getTestNumber().equals(testNumber))
                   node.retryCount++;
              
            }
        }
        
       
        
        TestSequence(List <Integer> requiredTests)
        {
            for( final Integer i : requiredTests)
            {
                TestNode node = new TestNode();
                node.setTestNumber(i);
                list.add(node);
                
             }
                
        }
        
        /*
         * TestNode contains the TestSequence Number, and the retryCount
         * 
         * */
        private class TestNode 
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

    }
    
    
    
}
