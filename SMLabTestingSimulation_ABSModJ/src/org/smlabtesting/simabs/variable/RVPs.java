package org.smlabtesting.simabs.variable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;
import org.smlabtesting.simabs.model.SMLabModel;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;

public class RVPs 
{
	SMLabModel model; // for accessing the clock
    // Data Models - i.e. random veriate generators for distributions
	// are created using Colt classes, define 
	// reference variables here and create the objects in the
	// constructor with seeds

	/* Data Models for implementing timing maps */
	protected Exponential interArrDist;  // Exponential distribution for interarrival times
	final double WMEAN1=10.0;

	// MYSTUFF

	public static final RandomGenerator DEFAULT_RNG = new Well19937a();
	
    private static final int HOURS_IN_DAY = 24;
    private static final int SECONDS_IN_HOUR = 3600;

    private static final int INCOMING_SAMPLE_ARRIVAL_DEVIATION = 25; //TODO: Undocumented in Deliverables.
    private static final int[] INCOMING_SAMPLE_RATES = new int[] {
        119, 107, 100, 113, 123, 116, 107, 121,
        131, 152, 171, 191, 200, 178, 171, 152,
        134, 147, 165, 155, 149, 134, 119, 116
    };

	
    private NormalDistribution deviationDistribution = new NormalDistribution(
            DEFAULT_RNG, 0, INCOMING_SAMPLE_ARRIVAL_DEVIATION
    );
	
	public double nextArrival() {
        int currentHour = (int) (( model.getClock() / SECONDS_IN_HOUR ) % HOURS_IN_DAY);
        int randomOffset = (int) deviationDistribution.sample();
        return Math.max(0, SECONDS_IN_HOUR/INCOMING_SAMPLE_RATES[currentHour] + randomOffset);
	}
	
	// ---
	
    // Constants
    private static final double[] CYCLE_TIME_BOUNDS = { 0.18 * 60, 0.23 * 60, 0.45 * 60 };

    // RNG
    private final TriangularDistribution cycleTimeDistribution = new TriangularDistribution(
            DEFAULT_RNG, CYCLE_TIME_BOUNDS[0], CYCLE_TIME_BOUNDS[1], CYCLE_TIME_BOUNDS[2]);
    /**
     * Generates a load/unload cycle duration using a triangular distribution.
     *
     * @return Machine cycle time in seconds.
     */
    public int uLoadUnloadMachineCycleTime() {
        return (int) cycleTimeDistribution.sample();
    }
    
    // --
    
    // RNG
    private static final TriangularDistribution stationTwoCleaningTimeDistribution = new TriangularDistribution(5.0*60, 6.0*60, 10.0*60);
    
    
    private static final int[] MACHINE_MBTF = IntStream.of(0, 14, -1, 9, 15, 16).map(i -> i * 3600).toArray();
    private static final int[] MACHINE_MBTR = IntStream.of(0, 11, -1, 7, 14, 13).map(i -> i * 60).toArray();
    


    public double uRepairTime(int stationId) {      
        return MACHINE_MBTR[stationId]; //TODO: No randomization yet.
    }

    public double generateFailureTime(int stationId) {
        // Machine two rarely fails.
        if (stationId == 2) {
            return Double.MAX_VALUE;
        }
        
        return MACHINE_MBTF[stationId]; //TODO: No randomization yet.
    }

    public int uCleaningTime() {
        return (int) stationTwoCleaningTimeDistribution.sample();
    }
    
    // --
    
//	public static final RandomGenerator DEFAULT_RNG = new Well19937a();
    private static final int[] SEQUENCE_ID = {0,1,2,3,4,5,6,7,8};
    private static final double[] PROBABILITIES = {0.09, 0.13, 0.15, 0.12, 0.07, 0.11, 0.14, 0.06, 0.13}; 
    
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
    
    private static final IntegerDistribution distribution = new EnumeratedIntegerDistribution(DEFAULT_RNG, SEQUENCE_ID, PROBABILITIES);
    
	public Deque<Integer> uSequenceOfTests() {
		Deque<Integer> list = new ArrayDeque<Integer>();
        IntStream.of(SEQUENCES[distribution.sample()]).forEach(list::add);
        return list;
	}
	
	// --
    
	// END MYSTUFF
	
	// Constructor
	public RVPs(SMLabModel model, Seeds sd) 
	{ 
		this.model = model; 
		// Set up distribution functions
		interArrDist = new Exponential(1.0/WMEAN1,  
				         new MersenneTwister(sd.seed1));
	}
	
	protected double duInput()  // for getting next value of uW(t)
	{
	    double nxtInterArr;

        nxtInterArr = interArrDist.nextDouble();
	    // Note that interarrival time is added to current
	    // clock value to get the next arrival time.
	    return(nxtInterArr+model.getClock());
	}



}
