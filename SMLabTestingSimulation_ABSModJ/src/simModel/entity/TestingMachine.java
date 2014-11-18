package simModel.entity;

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
    public enum TestingMachineState implements State {
        Idle, Testing, Cleaning, Repair;
    }
    
    // Constants
    private static final double[] TESTING_CYCLE_TIMES = DoubleStream.of(0, 0.77, 0.85 , 1.04, 1.24 , 1.7).map(i -> i * 60).toArray();
    private static final int[] MACHINE_MBTF = IntStream.of(0, 14, -1, 9, 15, 16).map(i -> i * 3600).toArray();
    private static final int[] MACHINE_MBTR = IntStream.of(0, 11, -1, 7, 14, 13).map(i -> i * 60).toArray();
    public static final int STATION_2_CLEANING_THRESHOLD = 300;
    
    // RNG
    private static final TriangularDistribution stationTwoCleaningTimeDistribution = new TriangularDistribution(5.0*60, 6.0*60, 10.0*60);
    
    // Relationships
    private final TestCellBuffer testCellBuffer;
    private final RacetrackLine racetrackLine;

    // Identifiers
    private final int stationId;
    private final int machineId; //TODO: Unused
    
    // Containers
    public SampleHolder sampleHolder = null;
    
    // Attributes
    public TestingMachineState status = Idle;
    public boolean testSuccess = false;
    public int timeUntilFailure = generateFailureTime();
    public int runTime = 0;
    public int completedTests = 0;
    
    // Constructs
    public TestingMachine(int stationId, int machineId) {
        this.stationId = stationId;
        this.machineId = machineId;
    }

    // Entity API

    @Override
    public String getGlance() {
        return String.format(
                "[TestingMachine] Currently %s",
                status
        );
    }

    // Private helper methods
    
    private int generateTestingTime() {
        return (int) TESTING_CYCLE_TIMES[stationId];
    }

    private int generateRepairTime() {      
        return MACHINE_MBTR[stationId]; //TODO: No randomization yet.
    }

    private int generateFailureTime() {
        // Machine two rarely fails.
        if (stationId == 2) {
            return Integer.MAX_VALUE;
        }
        
        return MACHINE_MBTF[stationId]; //TODO: No randomization yet.
    }

    private int generateCleaningTime() {
        return (int) stationTwoCleaningTimeDistribution.sample();
    }
    
}
