package org.smlabtesting.sim.domain.entity.loadunload;

import static org.smlabtesting.sim.domain.entity.loadunload.NewSamples.NewSamplesState.*;
import static org.smlabtesting.sim.executor.Simulation.DEFAULT_RNG;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Queue;
import org.smlabtesting.sim.domain.generic.State;

/**
 * Maps to Q.NewSamples.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class NewSamples extends Entity implements Queue<Sample> {
    // Constants
    private static final int HOURS_IN_DAY = 24;
    private static final int SECONDS_IN_HOUR = 3600;

    private static final int INCOMING_SAMPLE_ARRIVAL_DEVIATION = 25; //TODO: Undocumented in Deliverables.
    private static final int[] INCOMING_SAMPLE_RATES = new int[] {
        119, 107, 100, 113, 123, 116, 107, 121,
        131, 152, 171, 191, 200, 178, 171, 152,
        134, 147, 165, 155, 149, 134, 119, 116
    };

    // States
    protected enum NewSamplesState implements State {
        Default;
    }
    
    // Counters/Delays
    private int runTime = 0; // Runtime in seconds. Used to count current hour of operation.
    private int nextSampleIn = 0; // In how much seconds the next sample will arrive. 
    
    // RNG
    private NormalDistribution deviationDistribution = new NormalDistribution(
            DEFAULT_RNG, 0, INCOMING_SAMPLE_ARRIVAL_DEVIATION
    );
    
    // Containers
    private final Deque<Sample> samples = new ArrayDeque<Sample>();

    // Constructs
    public NewSamples() {}
    
    // Entity API
    @Override
    public void process() {
        // Initial State
        if (noState()) {
            setState(Default);

            // Prevent from always having a sample arriving in the first instant.
            nextSampleIn = nextArrival();
        }
    
        // Is it time for a new sample to arrive? 
        while (nextSampleIn == 0) {
            nextSampleIn = nextArrival();
            
            Sample sample = Sample.generateSample();
            samples.add(sample);
            simulation.addEntity(sample);
        }
        
        // Waiting for a new sample to arrive.
        nextSampleIn--;
        
        // Counting run time...
        runTime++;
    }
    
    @Override
    public String getGlance() {
        return String.format(
                "[NewSamples] Samples in line count: %d", 
                samples.size()
        );
    }

    // Queue API
    @Override
    public boolean hasNext() {
        return samples.peek() != null;
    }

    @Override
    public Sample next() {
        return samples.pop();
    }

    @Override
    public boolean hasVacancy() {
        return true; // Queue is unlimited.
    }
    
    @Override
    public void queue(final Sample entity) {
        samples.add(entity);
    }

    @Override
    public Iterator<Sample> iterator() {
        return samples.iterator();
    }
    
    // Helper Methods
    
    /**
     * Generates a waiting time until the next sample comes in seconds. Based
     * on the average arrival rate of the current hour.
     * 
     * TODO: Improve RNG technique. Not well distributed due to flooring at 0.
     * 
     * @return In how much seconds the next sample is going to arrive in.
     */
    private int nextArrival() {
        int currentHour = ( runTime / SECONDS_IN_HOUR ) % HOURS_IN_DAY;
        int randomOffset = (int) deviationDistribution.sample();
        return Math.max(0, SECONDS_IN_HOUR/INCOMING_SAMPLE_RATES[currentHour] + randomOffset);
    }
}
