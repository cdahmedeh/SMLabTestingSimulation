package org.smlabtesting.sim.domain.entity.testingassembly;


import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.entity.sampleholder.TestNode;
import org.smlabtesting.sim.domain.entity.sampleholder.TestSequence;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.State;
import org.smlabtesting.sim.logging.LogPrinter;

public class TestingMachine extends Entity {

    public Racetrack getRacetrack() {
        return racetrack;
    }

    public void setRacetrack(Racetrack racetrack) {
        this.racetrack = racetrack;
    }

    enum TestingMachineState implements State {
         Idle,Repair,Cleaning,Testing;
    }

    // relationships 
    SampleHolder sampleholder;
    TestCellBuffer testCellBufferSpace ;//= new TestCellBuffer();
   



    private RacetrackReturnQueue raceTrackReturnQueue ;//= new RacetrackReturnQueue();
    private Racetrack racetrack;
    //private RacetrackLine racetrackLine;
    private static final int RETRY_THRESHOLD =3;
    int completedTests; 
    boolean testSuccess;
    int runTime;
    int testType;   // from 1-5
    int timeuntilfailure;
    TestingMachineState testingmachineState;
    private int slotNumberOnRacetrack;
    private int groupNumber;
    private LogPrinter printer = new LogPrinter();
   
    
    
    public boolean testSample(SampleHolder holder)
    {
        // perform the test code , based on the sequence.. do a retry logic
        Sample sample = holder.removeSample();
        //TestNode  node = sample.getRemainingTests().getFirstTestSequence() ;
        // do the tests here
        // keep testing the sample till we get a 
        if(sample.getRemainingTests().getFirstTestSequence().getRetryCount()<= RETRY_THRESHOLD) 
        {      
           try{
               printer.println("Testing sampleId: "+ sample.getGlance()+" completed");
               sample.getRemainingTests().removeFirstTestSequence();
               printer.println("[Sample ]"+ sample.getGlance() + "Remaining tests:"+ sample.getRemainingTests().toString());
               // perform the test ??

           }
           catch(RuntimeException ex)
           {
               int retryCount = sample.getRemainingTests().getFirstTestSequence().getRetryCount();
               sample.getRemainingTests().getFirstTestSequence().setRetryCount(retryCount++);
               sampleholder.putSample(sample);
               // after adding the retry count , retest it by sending to the queue
               testCellBufferSpace.queue(sampleholder);
               return true;
               
           }
        }
                // if the retrycount is higher, send it across the racetrack queue
                sampleholder.putSample(sample);
                raceTrackReturnQueue.queue(sampleholder);
        
        
        return true;
        
    }

    @Override
    public String getGlance() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
   // SampleHolder sampleHolder;
   
        
    @Override
    public Handler[] generateHandlers() 
    {
        /*
        Handler idleHandler = new Handler(TestingMachineState.Idle)
        {
            @Override
            public boolean condition() 
            {
                if(testingmachineState == null ||  ( !testingmachineState.equals(TestingMachineState.Testing)
                  &&   raceTrackReturnQueue.getCurrentQueueSize()== 0 
                  &&   testCellBufferSpace.getCurrentQueueSize() == 0))
                {
                  testingmachineState = TestingMachineState.Idle;
                  return true;
                }
                return false;        
            }
            
                      
        };
        
        */
        Handler busyHandler = new Handler(TestingMachineState.Testing)
        {
            
            @Override
            public boolean condition() 
            {
                if(testCellBufferSpace.getCurrentQueueSize()> 0 || raceTrackReturnQueue.getCurrentQueueSize()>0)
                    return true;
                return false;
            }
            
            
            public void begin()
            {
                testingmachineState = TestingMachineState.Testing;
                if(testCellBufferSpace.hasNext())
                {
                    sampleholder = testCellBufferSpace.next();
                    testSample(sampleholder);
                }
                testingmachineState = TestingMachineState.Idle;
            }
         };
        
        return new Handler[]{busyHandler};
        

    }
    
    
    //Getter -setters
    
    
    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }
    
    
    public TestCellBuffer getTestCellBufferSpace() {
        return testCellBufferSpace;
    }

    public void setTestCellBufferSpace(TestCellBuffer testCellBufferSpace) {
        this.testCellBufferSpace = testCellBufferSpace;
    }

    public RacetrackReturnQueue getRaceTrackReturnQueue() {
        return raceTrackReturnQueue;
    }

    public void setRaceTrackReturnQueue(RacetrackReturnQueue raceTrackReturnQueue) {
        this.raceTrackReturnQueue = raceTrackReturnQueue;
    }
    
    
}
