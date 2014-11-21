package org.smlabtesting.simabs.variable;

import static java.lang.Double.MAX_VALUE;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;
import org.smlabtesting.simabs.model.SMLabModel;

public class RVPs {
	private SMLabModel model;
	private Seeds sd;

	public RVPs(SMLabModel model, Seeds sd) { 
		this.model = model;
		this.sd = sd; 
		
		initDistributions();
	}
   
	// RVP methods
    // Notice how everything is converted to int to shave off decimals. Our 
    // simulation is always incremented in whole seconds, and thus not needed 
    // to have decimal points.
	
	public int uSampleArrival() {
        int currentHour = (int) (( model.getClock() / 3600 ) % 24);
        return (int) sampleArrivalDist[currentHour].sample();
	}
	
    public int uLoadUnloadMachineCycleTime() {
        return (int) loadUnloadMachineCycleTimeDist.sample();
    }
	
    public int uCleaningTime() {
        return (int) stationTwoCleaningTimeDist.sample();
    }
    
    public int uFailureDuration(int stationId) {
        // Machine two rarely fails.
        if (stationId == 2) {
            return Integer.MAX_VALUE;
        }
        
        return (int) failureDist[stationId].sample();
    }
    
    public int uRepairTime(int stationId) {
    	// Machine 2 never fails.
    	if (stationId == 2) {
    		return 0;
    	}
    	
        return (int) repairDist[stationId].sample();
    }
    
	public Deque<Integer> uSequenceOfTests() {
		// TODO: If anyone finds an elegant way to do this and keep Java's
		//       stupid type erasure system out of the way, you'll get
		//       a chocolate bar.
		Deque<Integer> list = new ArrayDeque<Integer>();
		int sequenceNumber = sequenceDist.sample();
        IntStream.of(SEQUENCES[sequenceNumber]).forEach(list::add);
        return list;
	}
    
	public boolean uSampleRush() {
		return doubleGen.nextFloat() <= 0.07;
	}
	
	
    // Data Model Constants for RNGs

	// Test sequences and their probabilty of actually occuring.
    private static final int[] SEQUENCE_ID = {0,1,2,3,4,5,6,7,8};
    private static final double[] SEQUENCE_PROBABILITIES = {0.09, 0.13, 0.15, 0.12, 0.07, 0.11, 0.14, 0.06, 0.13};
    private static final int[][] SEQUENCES = {
        { 1, 2, 4, 5 },
        { 3, 4, 5 },
        { 1, 2, 3, 4 },
        { 4, 3, 2 },
        { 2, 5, 1 },
        { 4, 5, 2, 3 },
        { 1, 5, 3, 4 },
        { 5, 3, 1 },
        { 2, 4, 5 }
    };
	
    // Sample arrival rates per hour, not randomized. Needs to be divided by
    // 3600 to get arrival time.
	private static final double[] INCOMING_SAMPLE_RATES = new double[] {
        119, 107, 100, 113, 123, 116, 107, 121,
        131, 152, 171, 191, 200, 178, 171, 152,
        134, 147, 165, 155, 149, 134, 119, 116
    };
    
	// Station failure times and fix times. Integer.MAX_VALUE values indicate that they never
	// occur for said station. Both converted to seconds.
    private static final double[] MACHINE_MTBF = DoubleStream.of(MAX_VALUE, 14, MAX_VALUE, 9, 15, 16).map(i -> i * 3600).toArray();
    private static final double[] MACHINE_MTBR = DoubleStream.of(MAX_VALUE, 11, MAX_VALUE, 7, 14, 13).map(i -> i * 60).toArray();
	
	// Actual RNGs distribution models.
    // TODO: Update CM to use the new models.
    // TODO: Convert to CERN.
    
    // Generates a random sequence for incoming samples. 
    private IntegerDistribution sequenceDist;
    
	// Sample arrivals distribution, there is one for each hour of the day 0 to 23.
	private ExponentialDistribution[] sampleArrivalDist;
	
	// Load/Unload machine cycle times distribution.
    private TriangularDistribution loadUnloadMachineCycleTimeDist;

    // Cleaning time distribution for the second test cell.
    private TriangularDistribution stationTwoCleaningTimeDist;

    // Time until failure for each station.
    private ExponentialDistribution[] failureDist;
    
    // Repair time for a each station.
    private ExponentialDistribution[] repairDist;
    
    // Used to determine if a sample is a rush priority or not. 
    private RandomGenerator doubleGen = getAnotherRNG(); 
    
    private void initDistributions() {
        sequenceDist = 
        		new EnumeratedIntegerDistribution(getAnotherRNG(), SEQUENCE_ID, SEQUENCE_PROBABILITIES);
        
    	sampleArrivalDist = 
    			DoubleStream.of(INCOMING_SAMPLE_RATES)
    				.map(rate -> 3600/rate)
    				.mapToObj(ExponentialDistribution::new)
    				.toArray(ExponentialDistribution[]::new);
    	
    	loadUnloadMachineCycleTimeDist = 
        		new TriangularDistribution(getAnotherRNG(), 0.18 * 60, 0.23 * 60, 0.45 * 60);

        stationTwoCleaningTimeDist = 
        		new TriangularDistribution(getAnotherRNG(), 5.0*60, 6.0*60, 10.0*60);

        failureDist =
        		DoubleStream.of(MACHINE_MTBF)
        			.mapToObj(ExponentialDistribution::new)
        			.toArray(ExponentialDistribution[]::new);
        
        repairDist =
        		DoubleStream.of(MACHINE_MTBR)
        			.mapToObj(ExponentialDistribution::new)
        			.toArray(ExponentialDistribution[]::new);   	
    }
    

    // Generates a new random random generator when called based on the seed
    // list in Seeds
	// A set of variously seeded random number generators. One per distribution.
	private final RandomGenerator getAnotherRNG() {
		return new Well19937a(sd.next());
	}
}
