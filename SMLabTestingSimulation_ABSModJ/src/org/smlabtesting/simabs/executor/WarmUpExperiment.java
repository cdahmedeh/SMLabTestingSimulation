package org.smlabtesting.simabs.executor;

import org.smlabtesting.simabs.model.SMLabModel;
import org.smlabtesting.simabs.variable.Parameters;
import org.smlabtesting.simabs.variable.Seeds;

/**
 * Runs the warm up experiment to determine warm up time..  
 */
public class WarmUpExperiment {
	// Simulation configuration 
	private static final int NUM_RUNS = 50;					// Number of runs
	private static final int SAMPLE_INTERVAL = 3600;		// How often to take an output sample. 
	private static final int SAMPLE_COUNT = 24 * 10;		// How many samples to take (24*3600*10 = 10 days)
	
	public static void main(String[] args) {
		runWarmUp();
	}

	public static void runWarmUp(){
		double startTime=0.0, endTime=SAMPLE_INTERVAL*SAMPLE_COUNT;

		// Generate seeds per run.
		Seeds[] sds = new Seeds[NUM_RUNS];
		for(int i=0; i<NUM_RUNS ; i++) {
			sds[i] = new Seeds();
		}

		// The outputs of the run to be stored here.
		// We are only storing the percentages for failed samples.
		// Using a 3d-array with run mapping to the sample to the output.
		double outputs[][][] = new double[NUM_RUNS][SAMPLE_COUNT][2];
		
		// This runs are averaged at the end for processing and graphing.
		// Maps the sample to the averaged outputs.
		double averaged[][] = new double[SAMPLE_COUNT][2];
		
		// Create the simulation model and do some runs.
		// At every sample interval, stop the sim, and save the outputs.
		for(int run = 0 ; run < NUM_RUNS ; run++) {
			SMLabModel model; 
			Parameters parameters = new Parameters();
			model = new SMLabModel(startTime,endTime,sds[run], parameters, false);

			for (int sample = 0; sample < SAMPLE_COUNT; sample++) {
				model.setTimef(sample*SAMPLE_INTERVAL);
				model.runSimulation();
				outputs[run][sample][0] = model.output.percentageLateRegularSamples();
				outputs[run][sample][1] = model.output.percentageLateRushSamples();
				
				// Start adding the results so we can average them later.
				averaged[sample][0] += outputs[run][sample][0];
				averaged[sample][1] += outputs[run][sample][1];
			}
		}
		
		// Average the results and print the results.
		for (int sample = 0; sample < SAMPLE_COUNT; sample++) {
			averaged[sample][0] /= SAMPLE_COUNT;
			averaged[sample][1] /= SAMPLE_COUNT;

			System.out.print(sample);
			System.out.print('\t');
			System.out.print(averaged[sample][0]);
			System.out.print('\t');
			System.out.print(averaged[sample][1]);
			System.out.println();
		}
	}
}
