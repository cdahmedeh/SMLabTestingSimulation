package org.smlabtesting.simabs.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.smlabtesting.simabs.model.SMLabModel;
import org.smlabtesting.simabs.variable.Parameters;
import org.smlabtesting.simabs.variable.Seeds;

import cern.jet.random.engine.RandomSeedGenerator;

/**
 * Runs the simulation model. 
 */
public class Experiment {
   public static void main(String[] args) {
	   ExecutorService executor = Executors.newFixedThreadPool(4);
	   
	   // Number of runs to be performed.
       int NUMRUNS = 50; 
       
       // Start times and end times in seconds.
       int interval = 3600*24/4;
       int count = 30*4;
       double startTime=0.0, endTime=interval*count;
       
       // Generate seeds per run.
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       
       Seeds[] sds = new Seeds[NUMRUNS];
       for(int i=0; i<NUMRUNS ; i++) {
    	   sds[i] = new Seeds(rsg);
       }
       
       long start = System.currentTimeMillis();
       
       Runs runs = new Runs(count);
       
       // Create the simulation model and do some runs.
       for(int i = 0 ; i < NUMRUNS ; i++) {
    	  SMLabModel model;  
    	  Parameters parameters = new Parameters();
          model = new SMLabModel(startTime,endTime,sds[i], parameters, false);
          
          final int j = i; //TODO: Crazy voodoo magic!
          executor.execute(new Runnable() {

        	  @Override
        	  public void run() {
        		  Run run = new Run();
        		  for (int t = 0; t < count; t++) {
        			  model.setTimef(t*interval);
        			  model.runSimulation();
        			  run.add(t*interval, model.output);
        		  }

        		  runs.add(run);

        		  System.out.println(j);
        	  }
          });
          

          //          model.printOutputs();
       }
       
       try {
           executor.shutdown();
    	   executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
       } catch (InterruptedException e) {
    	   // TODO Auto-generated catch block
    	   e.printStackTrace();
       }
       
       runs.printAverages(interval);
       
       long end = System.currentTimeMillis();
       System.out.println(end - start);
   }
}
