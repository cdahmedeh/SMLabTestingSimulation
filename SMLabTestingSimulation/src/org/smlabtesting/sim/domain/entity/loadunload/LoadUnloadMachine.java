package org.smlabtesting.sim.domain.entity.loadunload;

import static org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine.LoadUnloadMachineState.LoadUnloadProcessing;
import static org.smlabtesting.sim.executor.Simulation.DEFAULT_RNG;

import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.entity.testingassembly.BoundedQueue;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
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
        LoadUnloadProcessing, TestState;
    }

    // RNG
    private final TriangularDistribution cycleTimeDistribution = new TriangularDistribution(
            DEFAULT_RNG, CYCLE_TIME_BOUNDS[0], CYCLE_TIME_BOUNDS[1], CYCLE_TIME_BOUNDS[2]);

    // Containers
   // private SampleHolder sampleHolder = null;
    
    // Relationships
    private final NewSamples newSamples;
    private final UnloadBuffer unloadBuffer;
    private final RacetrackLine racetrackLine;
    // use the racetrack line instead of racetrackQueue
    //private final BoundedQueue <SampleHolder>raceTrackQueue = new BoundedQueue<SampleHolder>(100);
    private final Queue <Sample> testedSamples;
    // Constructs
    public LoadUnloadMachine(final NewSamples newSamples, final UnloadBuffer unloadBuffer, final RacetrackLine racetrackLine, Queue<Sample > testedSamples) {
        this.newSamples = newSamples;
        this.unloadBuffer = unloadBuffer;
        this.racetrackLine = racetrackLine;
        this.testedSamples = testedSamples;
    }

    @Override
    public Handler[] generateHandlers() {
      /*  Handler testState = new Handler(LoadUnloadMachineState.TestState) {
            @Override
            public boolean condition() {
                System.out.println("...........");
                return false;
            }
        };
        
        */
        Handler loadunloadprocessing = 
            new Handler(LoadUnloadProcessing) {
                @Override
                public boolean condition() {
                    // Idle until there is a holder waiting in unload buffer. or a newly arrived sample does not come in for testing
                    return unloadBuffer.hasNext() || newSamples.hasNext();
                }
                
                @Override
                public void begin() 
                {
                    // Retrieve next sample holder in line in unload buffer.
                    boolean isNewSampleQueueNotEmpty =  newSamples.hasNext();
                    boolean isUnloadBufferNotEmpty  = unloadBuffer.hasNext();
                    
                    SampleHolder sampleHolder= null ;//= unloadBuffer.next();
                    
                    // if the unload buffer is not empty , then check if we have a new sample coming in. remove the previous 
                    //sampel from the unload buffer, and put in testedQueue. and also, put the new sample in the sampleHolder, adn pass it to the racetrackqueue
                    
                    if(!isUnloadBufferNotEmpty)
                    {
                        // if you dont have an empty sampleHolder, return..
                        //return;
                    }
                    
                    
                   if(isUnloadBufferNotEmpty)
                   {
                            sampleHolder = unloadBuffer.next();
                            if(sampleHolder!= null && sampleHolder.hasSample())
                                testedSamples.add( sampleHolder.removeSample());
                   }
                   if( isNewSampleQueueNotEmpty  && sampleHolder!= null)
                            sampleHolder.putSample(newSamples.next());
                   if(sampleHolder!= null)
                            racetrackLine.queue(sampleHolder);
                          //raceTrackQueue.queue(sampleHolder);
                                
                }
                
                @Override
                public int duration() {
                    // Simulate cycle time.
                    return generateCycleTime();
                }
                
                /*
                 * NOT USING THIS FUNCTION
                @Override
                public void end() {
                    // Queue to return to racetrack.
                    SampleHolder holder = raceTrackQueue.next();
                    if(holder!= null)
                        racetrackLine.queue(holder);
                 //   sampleHolder = null;
                }
                */
            
                
        };
        
        return new Handler [] { loadunloadprocessing};
    }
    
    @Override
    public String getGlance() {
        return String.format(
                "[LoadUnloadMachine]"
        );
    }
    
    // Public Methods
    
    /**
     * NOT USING THIS FUNCTION
     * Retrieves the currently processed sample holder. Null if none is being
     * processed.
     * 
     * @return A reference to the sample holder being processed.
     */
    
     /*public SampleHolder getSampleHolder() {
        return sampleHolder;
    }
      */
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
