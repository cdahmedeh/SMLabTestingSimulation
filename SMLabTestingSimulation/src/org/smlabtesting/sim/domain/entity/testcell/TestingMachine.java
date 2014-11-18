package org.smlabtesting.sim.domain.entity.testcell;

import static org.smlabtesting.sim.domain.entity.testcell.TestingMachine.TestingMachineState.*;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.smlabtesting.sim.domain.entity.racetrack.RacetrackLine;
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
    private static final double[] TESTING_CYCLE_TIMES = DoubleStream.of(0, 0.77, 0.85 , 1.04, 1.24 , 1.7).map(i -> i * 60).toArray();
    private static final int[] MACHINE_MBTF = IntStream.of(0, 14, Integer.MAX_VALUE, 9, 15, 16).map(i -> i * 3600).toArray();
    private static final int[] MACHINE_MBTR = IntStream.of(0, 11, Integer.MAX_VALUE, 7, 14, 13).map(i -> i * 60).toArray();
    private static final int STATION_2_CLEANING_THRESHOLD = 300;
    
    // RNG
    private static final TriangularDistribution stationTwoCleaningTimeDistribution = new TriangularDistribution(5.0*60, 6.0*60, 10.0*60);
    
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
                return status == Idle && (testCellBuffer.hasNext() || sampleHolder != null);
            }

            public void begin() {
                status = Testing;

                if (sampleHolder == null) {
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
                    racetrackLine.queue(sampleHolder);
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
        return String.format(
                "[TestingMachine] Currently %s",
                status
        );
    }

    // Private helper methods
    
    private int generateTestingTime() {
        return (int) TESTING_CYCLE_TIMES[machineId];
    }

    private int generateRepairTime() {
        return MACHINE_MBTR[machineId]; //TODO: No randomization yet.
    }

    private int generateFailureTime() {
        return MACHINE_MBTF[machineId]; //TODO: No randomization yet.
    }

    private int generateCleaningTime() {
        return (int) stationTwoCleaningTimeDistribution.sample();
    }
    
}
