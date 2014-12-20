package org.smlabtesting.simabs.executor;

import org.smlabtesting.simabs.model.SMLabModel;
import org.smlabtesting.simabs.types.ConfidenceInterval;
import org.smlabtesting.simabs.variable.Output;
import org.smlabtesting.simabs.variable.Parameters;
import org.smlabtesting.simabs.variable.Seeds;

/**
 * Runs the simulation model.  
 */
public class AutomatedExperiment {
	// Experiment configuration.
	private static final int NUM_RUNS = 10;					// Number of runs.
	private static final double CONFIDENCE_LEVEL = 0.90;	// Desired confidence level.

	private static final double START_TIME = 0;				// When to start
	private static final double END_TIME = 3600 * 24 * 20;	// When to stop. (20 days)
	private static final double WARM_UP = 3600 * 24 * 7;	// Warm up cut off point. (~7 days)

	private static final Parameters initParams = new Parameters(// Simulation parameters. To be changed per experiment.
			5,												// Number of empty allowed holders in the unload buffer
			new int[]{-1, 1, 1, 1, 1, 1}					// Number of machines per cell. First value is ignored
															// as LU machine has no testing machines.								
	);
		
	public static void main(String[] args) {
		Parameters parameters = initParams;
		
		// For every possible max empty holder configuration.
		for (int i = 5; i >= 1; i--) {
			System.out.println("=============== Maximum Empty Sample Holders: " + i);
			parameters.maxEmptyHolders = i;
			parameters.numCellMachines = new int[]{-1, 1, 1, 1, 1, 1};
		
			// Try adding 20 times or until we get good lateness.
			for (int j = 0; j < 20; j++) {
				// Run the sim...
				Output output = runExperiment(parameters);
				
				// Find station with largest bottleneck. Ignore load/unload.
				int largest = 0;
				int largestIndex = 0;
				for (int k = 1; k < output.totalFailedStationEntries.length; k++) {
					if (output.totalFailedStationEntries[k] > largest) {
						largest = output.totalFailedStationEntries[k];
						largestIndex = k;
					}
				}
				
				// Check if the lateness is good enough...
				if(output.percentageLateRegularSamples <= 0.1 && output.percentageLateRushSamples <= 0.02){
					System.out.println("Good lateness results found!");
					break;
				}

				// If the largest is zero, that also means that we can't improve our results anymore
				if(largest == 0){
					System.out.println("Cannot improve results anymore. No more bottlenecks in test cells!");
					break;
				}
				
				// Increase number of stations for that one.
				parameters.numCellMachines[largestIndex]++;
				
				System.out.println("===== Increasing number of machines for test cell C" + largestIndex);
			}
			
			System.out.println("===== Tried twenty times and nothing good found...");
		}
		
		
		
	}
	
	public static Output runExperiment(Parameters params){
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
		
		// Return the missed entries for decision making using the output container.
		return getAveragesAsOutput(percentagesLateRegularSamples, percentagesLateRushSamples, missedEntries);
	}

	private static void printResults(double[] percentagesLateRegularSamples, double[] percentagesLateRushSamples, int[] missedEntries) {
		System.out.println("----------RESULTS----------\n");
		System.out.print("Parameters: \nmaxEmptyHolders = "+initParams.maxEmptyHolders + "\n& numCellMachines = ");
		System.out.print("< "+initParams.numCellMachines[0]);
		for(int j = 1; j < 6; j++){
			System.out.print(", " + initParams.numCellMachines[j]);
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

		ConfidenceInterval intervalReg = new ConfidenceInterval(percentagesLateRegularSamples, CONFIDENCE_LEVEL);
		ConfidenceInterval intervalRush = new ConfidenceInterval(percentagesLateRushSamples, CONFIDENCE_LEVEL);

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
				intervalReg.getR());
		System.out.printf("    intervalRush %13.6f %18.6f %8.6f %8.6f %8.6f %14.6f\n", 
				intervalRush.getMean(), intervalRush.getStandardDeviation(), intervalRush.getZeta(), 
				intervalRush.getLowerCI(), intervalRush.getUpperCI(),
				intervalRush.getR());
		System.out.printf("-------------------------------------------------------------------------------------\n");
	}
	
	private static Output getAveragesAsOutput(double[] percentagesLateRegularSamples, double[] percentagesLateRushSamples, int[] missedEntries) {
		double avgLateRegSamples = 0;
		double avgLateRushSamples = 0;
		
		for(int i = 0; i < NUM_RUNS; i++){
			avgLateRegSamples += percentagesLateRegularSamples[i] / (double) NUM_RUNS;
			avgLateRushSamples += percentagesLateRushSamples[i] / (double) NUM_RUNS;
		}
		
		Output averageOutput = new Output(null);
		averageOutput.percentageLateRegularSamples = avgLateRegSamples;
		averageOutput.percentageLateRushSamples = avgLateRushSamples;	
		averageOutput.totalFailedStationEntries = missedEntries; //already average
		return averageOutput;
	}
}
