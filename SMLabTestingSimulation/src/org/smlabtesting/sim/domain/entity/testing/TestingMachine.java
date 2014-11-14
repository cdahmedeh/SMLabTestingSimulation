package org.smlabtesting.sim.domain.entity.testing;

import static org.smlabtesting.sim.executor.Simulation.DEFAULT_RNG;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.State;

//TODO: change generateRepairTime()
// generateCleaningTime()
//generateFailureTime()
public class TestingMachine extends Entity {

    private final TestCellBuffer testCellBuffer;// = new TestCellBuffer(
    final RacetrackLine racetrackLine;
    SampleHolder sampleHolder;
    TestingMachineStatus status;

    
    public TestingMachine(RacetrackLine racetrackLine, TestCellBuffer testCellBuffer, int stationid, int testmachineid )
    {
        this.racetrackLine = racetrackLine;
        this.testCellBuffer = testCellBuffer;
        this.stationid = stationid;
        this.testMachineid = testmachineid;
        
    }
   
    // NOT SURE WHAT IS THIS???
    private int timeUntilFailure=0;
    private int runTime;
    private boolean testSuccess= false;
    private int completedTests = 0;
    private final int testMachineid;
    private final int stationid;  // this is groupid 
    private static final double[] CYCLE_TIME_BOUNDS = { 0.18 * 60, 0.23 * 60,
            0.45 * 60 };

    // Got this from the loadUnloadMachine
    private final TriangularDistribution cycleTimeDistribution = new TriangularDistribution(
            DEFAULT_RNG, CYCLE_TIME_BOUNDS[0], CYCLE_TIME_BOUNDS[1],
            CYCLE_TIME_BOUNDS[2]);

    private enum TestingMachineStatus implements State {
        Idle, Testing, Cleaning,Repair;
    }

    /**
     * Generates a load/unload cycle duration using a triangular distribution.
     *
     * @return Machine cycle time in seconds.
     */
    private int generateCycleTime() {
        return (int) cycleTimeDistribution.sample();
    }

    private void performTest(SampleHolder holder) {

        Sample sample = holder.removeSample();

    }
    private int generateRepairTime(){
        return 0;
    }
    
    private int generateFailureTime(){
        return 0;
    }
    
    private int generateCleaningTime(){
        return 0;
    }

    private int generateTimingsForEvent(){
        return 0;
    }
    public Handler[] generateHandlers() {
        Handler idleHandler = new Handler(TestingMachineStatus.Idle)
        {
            public boolean condition() {
                
            return   ( status == TestingMachineStatus.Idle) && (testCellBuffer.hasNext() || sampleHolder.hasSample());
                
            }
            
            public void begin() {
                status = TestingMachineStatus.Testing;
                if(testCellBuffer.hasNext() && sampleHolder.hasSample())
                {
                    sampleHolder = testCellBuffer.next();
                    
                    
                }
                // why are we doing this???
                int testingTime = generateCycleTime();
                
                if(timeUntilFailure > testingTime)
                {
                    runTime= testingTime;
                    testSuccess  = true;
                    
                }
                else
                {
                    runTime = timeUntilFailure;
                    testSuccess= false;
                }
                
                    
            }
            
            
            
            @Override
            public int duration() {
                // Simulate cycle time.
                return generateCycleTime();
            }
            
            public void end() {
                if(testSuccess)
                {
                    SampleHolder holder = sampleHolder;
                    racetrackLine.queue(holder);
                    sampleHolder = null;
                    status = TestingMachineStatus.Idle;
                    completedTests++;
                    
                    if(stationid == 2  && (completedTests% STATION_2_CLEANING_THRESHOLD== 0))
                        status = TestingMachineStatus.Cleaning;
                }
                else{
                    status = TestingMachineStatus.Repair;
                }
            }
 
        };
        
        
        Handler repairHandlr = new Handler(TestingMachineStatus.Repair) 
        {
            public boolean condition() {
                return status == TestingMachineStatus.Repair;
            }
            
            public int duration() {
                return generateRepairTime();
            }
            
            public void end() {
                timeUntilFailure = generateFailureTime();
                status = TestingMachineStatus.Idle;
                
            }
            
        };
        
        Handler cleaningHandlr = new Handler(TestingMachineStatus.Repair) 
        {
            public boolean condition() {
                return status == TestingMachineStatus.Cleaning;
            }
            
            public int duration() {
                return generateCleaningTime();
            }
            
            public void end() {
                status = TestingMachineStatus.Idle;
                
            }
            
        };
        
        return new Handler[]{idleHandler, repairHandlr, cleaningHandlr};
        
    }

    //TODO
    private static final int STATION_2_CLEANING_THRESHOLD= 300;
    @Override
    public String getGlance() {
        // TODO Auto-generated method stub
        return null;
    }

}
