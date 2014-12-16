package org.smlabtesting.simabs.executor;

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
       int NUMRUNS = 1; 
       
       // Start times and end times in seconds.
       double startTime=0.0, endTime=3600*24*10;
       
       // Generate seeds per run.
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       
       Seeds[] sds = new Seeds[NUMRUNS];
       for(int i=0; i<NUMRUNS ; i++) {
    	   sds[i] = new Seeds(rsg);
       }
       
       long start = System.currentTimeMillis();
       
       // Create the simulation model and do some runs.
       for(int i = 0 ; i < NUMRUNS ; i++) {
    	  SMLabModel model;  
    	  Parameters parameters = new Parameters();
          model = new SMLabModel(startTime,endTime,sds[i], parameters, false);
          model.runSimulation();
//          model.printOutputs();
       }
       
       long end = System.currentTimeMillis();
       System.out.println(end - start);
   }
}
