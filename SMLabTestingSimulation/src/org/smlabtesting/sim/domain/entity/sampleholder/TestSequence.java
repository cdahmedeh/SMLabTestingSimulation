package org.smlabtesting.sim.domain.entity.sampleholder;

import java.util.LinkedList;
import java.util.List;

public class TestSequence
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
               node.setRetryCount(node.getRetryCount()+1);
          
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
    
    public String toString()
    {
        
        StringBuilder str = new StringBuilder();
        for(TestNode node: list)
        {
            str.append( "[TestCode: ]  " + node.getTestNumber() +" [RetryCount ] "+ node.getRetryCount() );
            str.append( "\n");
        }
        return str.toString();
    }


}
