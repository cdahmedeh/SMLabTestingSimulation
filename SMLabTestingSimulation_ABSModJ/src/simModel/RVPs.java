package simModel;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;

public class RVPs 
{
	ModelName model; // for accessing the clock
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
    public int generateCycleTime() {
        return (int) cycleTimeDistribution.sample();
    }
    
    // --
    
    // RNG
    private static final TriangularDistribution stationTwoCleaningTimeDistribution = new TriangularDistribution(5.0*60, 6.0*60, 10.0*60);
    
    private static final double[] TESTING_CYCLE_TIMES = DoubleStream.of(0, 0.77, 0.85 , 1.04, 1.24 , 1.7).map(i -> i * 60).toArray();
    private static final int[] MACHINE_MBTF = IntStream.of(0, 14, -1, 9, 15, 16).map(i -> i * 3600).toArray();
    private static final int[] MACHINE_MBTR = IntStream.of(0, 11, -1, 7, 14, 13).map(i -> i * 60).toArray();
    
    
    public double generateTestingTime(int stationId) {
        return TESTING_CYCLE_TIMES[stationId];
    }

    public double generateRepairTime(int stationId) {      
        return MACHINE_MBTR[stationId]; //TODO: No randomization yet.
    }

    public double generateFailureTime(int stationId) {
        // Machine two rarely fails.
        if (stationId == 2) {
            return Double.MAX_VALUE;
        }
        
        return MACHINE_MBTF[stationId]; //TODO: No randomization yet.
    }

    public int generateCleaningTime() {
        return (int) stationTwoCleaningTimeDistribution.sample();
    }
    
	// END MYSTUFF
	
	// Constructor
	public RVPs(ModelName model, Seeds sd) 
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
