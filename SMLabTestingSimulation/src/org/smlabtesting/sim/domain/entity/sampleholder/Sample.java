package org.smlabtesting.sim.domain.entity.sampleholder;

import java.sql.Time;
import java.sql.Timestamp;
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
    
    //attributes
    private Priority priority = Priority.MEDIUM;
    private TestSequence testsRemaining ;
    private Timestamp startTime;
    private int sampleId;
    private static int counter=0;
    // States
    protected enum SampleState implements State {
        Default;
    }

 // for medium or rush samples( High)
    public enum Priority{
        RUSH, MEDIUM; 
    }
    
    
    public Priority getSamplePriority(){
        return priority;
    }
    public void setRushSample(){
        priority = Priority.RUSH;
    }
    
    // Factories
    public static Sample generateSample() {
        return new Sample();
    }
    
    // Constructs
    public Sample() 
    {
        sampleId= counter++;
        startTime = new Timestamp(System.currentTimeMillis());
    }    
    
    // Entity API
    public Handler[] generateHandlers() {
        return new Handler[]{};
    };

    @Override
    public String getGlance() {
        return String.format(
                "[Sample]" + sampleId +" [Generated Time:   "+ startTime+"]"
        );
    }
    
    // TestSequence is used for checking the testsequence associated with the sample
    // It has a retrycount for the testing done in the cell
    // each Sample has to have a testSequence
   
    public void setTestsRemaining(List <Integer> testRemaining) 
    {
        if(testRemaining == null || testRemaining.isEmpty())
            return;
        this.testsRemaining = new TestSequence(testRemaining);
    }

    public TestSequence getRemainingTests()
    {
        return testsRemaining;
                
    }
    
    
    
    
}
