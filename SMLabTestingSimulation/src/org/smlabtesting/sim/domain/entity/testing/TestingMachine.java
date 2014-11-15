package org.smlabtesting.sim.domain.entity.testing;

import static org.smlabtesting.sim.domain.entity.testing.TestingMachine.TestingMachineState.*;

import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to RC.TestingMachine
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class TestingMachine extends Entity {
    // States
    protected enum TestingMachineState implements State {
        Idle, Testing, Cleaning, Repair;
    }
    
    // Constants
    private static final int STATION_2_CLEANING_THRESHOLD = 300;
    
    // Relationships
    private final TestCellBuffer testCellBuffer;
    private final RacetrackLine racetrackLine;

    // Identifiers
    private final int stationId;
    private final int machineId; //TODO: Unused
    
    // Containers
    private SampleHolder sampleHolder = null;
    
    // Attributes
    private TestingMachineState status = Idle;
    private boolean testSuccess = false;
    private int timeUntilFailure = Integer.MAX_VALUE;
    private int runTime = 0;
    private int completedTests = 0;
    
    // Constructs
    public TestingMachine(RacetrackLine racetrackLine, TestCellBuffer testCellBuffer, int stationId, int machineId) {
        this.racetrackLine = racetrackLine;
        this.testCellBuffer = testCellBuffer;
        this.stationId = stationId;
        this.machineId = machineId;
    }

    // Entity API
    
    public Handler[] generateHandlers() {
        Handler testingHandler = new Handler(Testing) {
            public boolean condition() {
                return status == Idle && (testCellBuffer.hasNext() || sampleHolder.hasSample());
            }

            public void begin() {
                status = Testing;

                if (testCellBuffer.hasNext() && sampleHolder.hasSample()) {
                    sampleHolder = testCellBuffer.next();
                }

                int testingTime = generateTestingTime();

                if (timeUntilFailure > testingTime) {
                    runTime = testingTime;
                    testSuccess = true;
                } else {
                    runTime = timeUntilFailure;
                    testSuccess = false;
                }
            }

            @Override
            public int duration() {
                return runTime;
            }

            public void end() {
                if (testSuccess) {
                    SampleHolder holder = sampleHolder;
                    racetrackLine.queue(holder);
                    sampleHolder = null;
                    status = Idle;
                    completedTests++;

                    if (stationId == 2 && (completedTests % STATION_2_CLEANING_THRESHOLD == 0)) {
                        status = Cleaning;
                    }
                } else {
                    status = Repair;
                }
            }
        };

        Handler repairHandler = new Handler(Repair) {
            public boolean condition() {
                return status == Repair;
            }

            public int duration() {
                return generateRepairTime();
            }

            public void end() {
                timeUntilFailure = generateFailureTime();
                status = Idle;
            }
        };

        Handler cleaningHandler = new Handler(Repair) {
            public boolean condition() {
                return status == Cleaning;
            }

            public int duration() {
                return generateCleaningTime();
            }

            public void end() {
                status = Idle;
            }
        };

        return new Handler[] {testingHandler, repairHandler, cleaningHandler};
    }

    @Override
    public String getGlance() {
        return "[TestingMachine]";
    }

    // Private helper methods
    
    private int generateTestingTime() {
        return 0;
    }

    private int generateRepairTime() {
        return 0;
    }

    private int generateFailureTime() {
        return 0;
    }

    private int generateCleaningTime() {
        return 0;
    }
    
}
