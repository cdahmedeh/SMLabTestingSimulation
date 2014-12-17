package org.smlabtesting.simabs.executor;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.smlabtesting.simabs.model.SMLabModel;
import org.smlabtesting.simabs.variable.Parameters;
import org.smlabtesting.simabs.variable.Seeds;

import cern.jet.random.engine.RandomSeedGenerator;

/**
 * Runs the simulation model. 
 */
public class Experiment {
   public static void main(String[] args) {
	   // Number of runs to be performed.
       int NUMRUNS = 50; 
       
       // Generate seeds per run.
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       
       Seeds[] sds = new Seeds[NUMRUNS];
       for(int i=0; i<NUMRUNS ; i++) {
    	   sds[i] = new Seeds(rsg);
       }
       
       long start = System.currentTimeMillis();
       
       NUMRUNS = 50;
       double startTime = 0;
       double endTime = 3600 * 24 * 15;
       runExperiment(sds, new Parameters(5, new int[]{1,1,1,1,1,1}), startTime, endTime, NUMRUNS);
   }
   
   public static void runExperiment(Seeds[] seeds, Parameters params, double WARM_UP_END_T, double RUN_END_T, int NUMRUNS){
	   double percentageRegularSamples = 0;
	   double percentageRushSamples = 0;
	   int[] missedEntries = new int[] {0,0,0,0,0,0};
	   
	   for(int i = 0; i < NUMRUNS; i++){
		   SMLabModel model = new SMLabModel(WARM_UP_END_T, RUN_END_T, seeds[i], params, false);
		   model.runSimulation();
		   model.output.percentageLateRegularSamples();
		   model.output.percentageLateRushSamples();
		   percentageRegularSamples += model.output.percentageLateRegularSamples / (double) NUMRUNS;
		   percentageRushSamples += model.output.percentageLateRushSamples / (double) NUMRUNS;
		   for(int j = 1; j < 6; j++){
			   missedEntries[j] += model.output.totalFailedStationEntries[j] / (double) NUMRUNS;
		   }
	   }
	   System.out.println("----------RESULTS----------\n");
	   System.out.print("Parameters: \nmaxEmptyHolders = "+params.maxEmptyHolders + "\n& numCellMachines = ");
	   System.out.print("< "+params.numCellMachines[0]);
	   for(int j = 0; j < 6; j++){
		  System.out.print(", " + params.numCellMachines[j]);
	   }
	   System.out.println(" >\n");
	   System.out.println("Number of Runs= "+ NUMRUNS);
	   System.out.println("-----------");
	   
	   System.out.println("% Regular late: "+ percentageRegularSamples);
	   System.out.println("% Rush late: "+ percentageRushSamples);
	   System.out.print("< "+missedEntries[0]);
	   for(int j = 1; j < 6; j++){
		  System.out.print(", " + missedEntries[j]);
	   }
	   System.out.println(" >");
   }
}
