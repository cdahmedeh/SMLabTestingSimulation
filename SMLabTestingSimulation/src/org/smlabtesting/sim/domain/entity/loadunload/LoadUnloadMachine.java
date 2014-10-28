package org.smlabtesting.sim.domain.entity.loadunload;

import static org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine.LoadUnloadMachineState.*;
import static org.smlabtesting.sim.executor.Simulation.DEFAULT_RNG;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to R.LoadUnloadMachine 
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class LoadUnloadMachine extends Entity {
    // Constants
    private static final double[] CYCLE_TIME_BOUNDS = { 0.18 * 60, 0.23 * 60, 0.45 * 60 };

    // States
    protected enum LoadUnloadMachineState implements State {
        Waiting, LoadUnloadProcessing;
    }

    // RNG
    private final TriangularDistribution cycleTimeDistribution = new TriangularDistribution(
            DEFAULT_RNG, CYCLE_TIME_BOUNDS[0], CYCLE_TIME_BOUNDS[1], CYCLE_TIME_BOUNDS[2]);

    // Containers
    private SampleHolder sampleHolder = null;
    
    // Relationships
    private final NewSamples newSamples;
    private final UnloadBuffer unloadBuffer;
    private final RacetrackLine racetrackLine;

    // Constructs
    public LoadUnloadMachine(final NewSamples newSamples, final UnloadBuffer unloadBuffer, final RacetrackLine racetrackLine) {
        this.newSamples = newSamples;
        this.unloadBuffer = unloadBuffer;
        this.racetrackLine = racetrackLine;
    }

    // Entity API
    @Override
    public void process() {
        // Initial State
        if (noState()) {
            setState(Waiting);
        }

        // Idle until there is a holder waiting in unload buffer.
        if (isState(Waiting)) {
            if (unloadBuffer.hasNext()) {
                // Do the load/unload processing
                setState(LoadUnloadProcessing);
                
                // Retrieve next sample holder in line in unload buffer.
                sampleHolder = unloadBuffer.next();

                // If sample holder has a sample, remove it.
                if (sampleHolder.hasSample()) {
                    Sample removedSample = sampleHolder.removeSample();
                    simulation.removeEntity(removedSample);
                }

                // If a new samples is in line to be processed, insert into holder.
                if (newSamples.hasNext()) {
                    sampleHolder.putSample(newSamples.next());
                }
        
                pause(generateCycleTime()); // Simulate cycle time.
            }
        }

        // Loading/Unloading wait step.
        if (isState(LoadUnloadProcessing)) {
            // Queue to return to racetrack.
            racetrackLine.queue(sampleHolder);
            sampleHolder = null;            

            setState(Waiting);
            return;
        }
    }
    
    @Override
    public String getGlance() {
        return String.format(
                "[LoadUnloadMachine] State: %s", 
                getState()
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