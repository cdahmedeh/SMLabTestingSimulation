package org.smlabtesting.sim.domain.entity.testingassembly;

import java.util.ArrayList;
import java.util.List;

import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.entity.testingassembly.TestingMachine.TestingMachineState;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.State;

public class TestCell extends Entity {

    private Racetrack racetrack;

    private enum TestCellState implements State {
        Idle, Busy;
    }

    private final List<TestingMachine> machines = new ArrayList<TestingMachine>();
    // every testCell will have a unqiue groupId
    private int testNumber; // TestMachineManager.generateGroupID();

    TestCellBuffer testCellBuffer = new TestCellBuffer();
    RacetrackReturnQueue racetrackReturnQueue = new RacetrackReturnQueue();

    private int enterSlot;
    private int exitSlot;
   public int getEnterSlot() {
        return enterSlot;
    }

    public void setEnterSlot(int enterSlot) {
        this.enterSlot = enterSlot;
    }

    public int getExitSlot() {
        return exitSlot;
    }

    public void setExitSlot(int exitSlot) {
        this.exitSlot = exitSlot;
    }

    /* public TestCell() {
        setEnterExitSlots();

    }
*/
    public TestCell(Racetrack racetrack, int groupNumber)
    {
        this.testNumber  = groupNumber;
        this.racetrack = racetrack;
        //setEnterExitSlots();   
    }
    
    // use this method to add the TestMachine to the cell
    public void addTestMachineToCell(TestingMachine machine) 
    {
        machine.setRaceTrackReturnQueue(racetrackReturnQueue);
        machine.setTestCellBufferSpace(testCellBuffer);
        machine.setGroupNumber(testNumber);
        machine.setRacetrack(racetrack);
        // Adds the entry-exit slots to machine in the test cell
        assignSlots( true);
        assignSlots( false);
        
        machines.add(machine);

    }

    // used to merge the sampleHolders in and out of the racetrack
    // give the user to enter the slotNumber, if its already taken, it will
    // readjust to next slot
    private TestCellState testCellState;

    // So it resets to the next available slot on racetrack
    private static boolean slots [] = new boolean [Racetrack.BELT_SLOTS_COUNT];
    static
    {
        slots[Racetrack.LOAD_UNLOAD_ENTRANCE]  = true;
        slots[Racetrack.LOAD_UNLOAD_EXIT] = true;
    }
    
    // pass the machine, and assign it to any slots
    private void assignSlots(boolean isEntrySlot)
    {
        boolean isSlotAssigned= false;
        int counter =0;
        do{
            int slotNumber = (int )(( Math.random() *100) % Racetrack.BELT_SLOTS_COUNT) ;
            if(!slots[slotNumber])
            {
                slots[slotNumber] = true;
                if(isEntrySlot)
                    setEnterSlot(slotNumber);
                else 
                    setExitSlot(slotNumber);
                isSlotAssigned = true;
                
            }
        }while(!isSlotAssigned);
                
    }
    
    
    private boolean checkforTestSample( int slotNumberOnRacetrack)
    {
        if(!racetrack.isVacant(slotNumberOnRacetrack) )
        {
            // we just peek for the sample on the racetrack.. and add to queue incase needed
            Sample sample = racetrack.getSample(slotNumberOnRacetrack);
            if(sample == null||  sample.getRemainingTests() == null || sample.getRemainingTests().getFirstTestSequence()== null)
                return false;
            if(sample.getRemainingTests().getFirstTestSequence().getTestNumber() == testNumber)
                return true;
        }
        return false;
    }
    private void addSampleToTestBufferQueue()
    {
       if( checkforTestSample(enterSlot))
           testCellBuffer.queue( racetrack.take(enterSlot)) ;
     }
            

    @Override
    public Handler[] generateHandlers() 
    {
        /*
         * 
        Handler idleHandlr = new Handler(TestCellState.Idle) 
        {
            public boolean condition() 
            {
                if (testCellState == null
                    || ( !testCellBuffer.hasNext() && !racetrackReturnQueue.hasNext()) )
                    return true;
                return false;
            }

            public void begin() 
            {
                testCellState = TestCellState.Idle;
                if(checkforTestSample(enterSlot)) 
                return;
            }

        };
*/
        // check for the incoming sample on the racetrack, and enter only if it
        // matches
        // move the sample from the racetrack to the testCellBuffer
        // and then from the ReturnQueue to the racetrack
        Handler busyHandler = new Handler(TestCellState.Busy) 
        {
            public boolean condition() 
            {
                if (testCellState == null)
                    testCellState  = TestCellState.Busy;
                
               if(checkforTestSample(enterSlot) || (  racetrackReturnQueue.hasNext() && racetrack.isVacant(exitSlot)) ) 
                    return true;

               return false;
              
            }

            public void begin() 
            {
                addSampleToTestBufferQueue();
                if (racetrack.isVacant(exitSlot)
                        && racetrackReturnQueue.hasNext()) 
                {
                    SampleHolder holder = racetrackReturnQueue.next();
                    racetrack.setSlot(exitSlot, holder);
                }
                //testCellState = TestCellState.Idle;

            }

        };

        return new Handler[] { busyHandler };

    }

    @Override
    public String getGlance() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

}
