package simModel.entity;

import static org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine.LoadUnloadMachineState.LoadUnloadProcessing;
import static org.smlabtesting.sim.executor.Simulation.DEFAULT_RNG;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.smlabtesting.sim.domain.entity.racetrack.RacetrackLine;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to R.LoadUnloadMachine 
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class LoadUnloadMachine extends Entity {
    // Constants
    private static final double[] CYCLE_TIME_BOUNDS = { 0.18 * 60, 0.23 * 60, 0.45 * 60 };

    // States
    protected enum LoadUnloadMachineState implements State {
        LoadUnloadProcessing;
    }

    // RNG
    private final TriangularDistribution cycleTimeDistribution = new TriangularDistribution(
            DEFAULT_RNG, CYCLE_TIME_BOUNDS[0], CYCLE_TIME_BOUNDS[1], CYCLE_TIME_BOUNDS[2]);

    // Attributes
    private boolean busy = false; //TODO: Unused.   
    
    // Containers
    public SampleHolder sampleHolder = null;
    
    // Relationships
    private final NewSamples newSamples;
    private final UnloadBuffer unloadBuffer;
    private final RacetrackLine racetrackLine;
    
    @Override
    public String getGlance() {
        return String.format(
                "[LoadUnloadMachine]"
        );
    }
    
    // Public Methods
    
    /**
     * Retrieves the currently processed sample holder. Null if none is being
     * processed.
     * 
     * @return A reference to the sample holder being processed.
     */
    public SampleHolder getSampleHolder() {
        return sampleHolder;
    }

    // Helper Methods

    /**
     * Generates a load/unload cycle duration using a triangular distribution.
     *
     * @return Machine cycle time in seconds.
     */
    private int generateCycleTime() {
        return (int) cycleTimeDistribution.sample();
    }

}
