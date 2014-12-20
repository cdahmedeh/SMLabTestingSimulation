package org.smlabtesting.simabs.executor;

import org.smlabtesting.simabs.model.SMLabModel;
import org.smlabtesting.simabs.types.SuperConfidenceInterval;
import org.smlabtesting.simabs.variable.Parameters;
import org.smlabtesting.simabs.variable.Seeds;

/**
 * Runs the simulation model.  
 */
public class Experiment {
	// Experiment configuration.
	private static final int NUM_RUNS = 10;					// Number of runs.
	private static final double CONFIDENCE_LEVEL = 0.90;	// Desired confidence level.

	private static final double START_TIME = 0;				// When to start
	private static final double END_TIME = 3600 * 24 * 4;	// When to stop. (5 days)
	private static final double WARM_UP = 3600 * 24 * 1;		// Warm up cut off point.

	private static final Parameters params = new Parameters(// Simulation parameters. To be changed per experiment.
			3,												// Number of empty allowed holders in the unload buffer
			new int[]{-1, 4, 5, 6, 7, 8}					// Number of machines per cell. First value is ignored
															// as LU machine has no testing machines.								
	);
		
	public static void main(String[] args) {
		runExperiment();
	}
	
	public static void runExperiment(){
		// Generate some seeds		
		Seeds[] sds = new Seeds[NUM_RUNS];
		for(int i=0; i<NUM_RUNS ; i++) {
			sds[i] = new Seeds();
		}

		// Outputs to be stored and averaged per run.
		double[] percentagesLateRegularSamples = new double [NUM_RUNS];
		double[] percentagesLateRushSamples = new double [NUM_RUNS];
		int[] missedEntries = new int[] {0,0,0,0,0,0};

		// Run the experiment multiple times.
		for(int i = 0; i < NUM_RUNS; i++){
			SMLabModel model = new SMLabModel(START_TIME, END_TIME, sds[i], params, false);
			
			// Prepare the sim for the cut-off point to reset some results after
			// the warmup time is reached.
			if (WARM_UP > 0) {
				model.setTimef(WARM_UP);			
				model.runSimulation();
			}

			// Once the warmup time is reached, reset outputs and 
			// finish off the rest of the sim.
			model.output.reset();
			model.setTimef(END_TIME);
			model.runSimulation();
			
			// Collect results.
			percentagesLateRegularSamples[i] += model.output.percentageLateRegularSamples();
			percentagesLateRushSamples[i] += model.output.percentageLateRushSamples();
			for(int j = 0; j < 6; j++){
				missedEntries[j] += model.output.totalFailedStationEntries[j] / (double) NUM_RUNS;
			}
		}
		
		// Print the results.
		printResults(percentagesLateRegularSamples, percentagesLateRushSamples,	missedEntries);
	}

	private static void printResults(double[] percentagesLateRegularSamples, double[] percentagesLateRushSamples, int[] missedEntries) {
		System.out.println("----------RESULTS----------\n");
		System.out.print("Parameters: \nmaxEmptyHolders = "+params.maxEmptyHolders + "\n& numCellMachines = ");
		System.out.print("< "+params.numCellMachines[0]);
		for(int j = 1; j < 6; j++){
			System.out.print(", " + params.numCellMachines[j]);
		}
		System.out.println(" >\n");
		System.out.println("Number of Runs= "+ NUM_RUNS);
		System.out.println("-----------");


		System.out.print("< "+missedEntries[0]);
		for(int j = 1; j < 6; j++){
			System.out.print(", " + missedEntries[j]);
		}
		System.out.println(" >");
		System.out.println("Run #\t%LateRegularSample\t%LateRushSample");
		for(int i = 0; i < NUM_RUNS; i++){
			System.out.println((i+1)+"\t"+percentagesLateRegularSamples[i]+"\t"+percentagesLateRushSamples[i]);
		}
		double avgLateRegSamples = 0;
		double avgLateRushSamples = 0;

		for(int i = 0; i < NUM_RUNS; i++){
			avgLateRegSamples += percentagesLateRegularSamples[i] / (double) NUM_RUNS;
			avgLateRushSamples += percentagesLateRushSamples[i] / (double) NUM_RUNS;
		}

		SuperConfidenceInterval intervalReg = new SuperConfidenceInterval(percentagesLateRegularSamples, CONFIDENCE_LEVEL);
		SuperConfidenceInterval intervalRush = new SuperConfidenceInterval(percentagesLateRushSamples, CONFIDENCE_LEVEL);

		if(avgLateRegSamples <= 0.1 && avgLateRushSamples <= 0.02){
			System.out.println("WOOHOO percent late OK");
		}else{
			System.out.println("Percent late too high.");
		}

		System.out.printf("-------------------------------------------------------------------------------------\n");
		System.out.printf("Comparison    Point estimate(ybar(n))  s(n)     zeta   CI Min   CI Max |zeta/ybar(n)|\n");
		System.out.printf("-------------------------------------------------------------------------------------\n");
		System.out.printf("    intervalReg %13.6f %18.6f %8.6f %8.6f %8.6f %14.6f\n",
				intervalReg.getMean(), intervalReg.getStandardDeviation(), intervalReg.getZeta(), 
				intervalReg.getLowerCI(), intervalReg.getUpperCI(),
				intervalRush.getR());
		System.out.printf("    intervalRush %13.6f %18.6f %8.6f %8.6f %8.6f %14.6f\n", 
				intervalRush.getMean(), intervalRush.getStandardDeviation(), intervalRush.getZeta(), 
				intervalRush.getLowerCI(), intervalRush.getUpperCI(),
				intervalRush.getR());
		System.out.printf("-------------------------------------------------------------------------------------\n");
	}
}
