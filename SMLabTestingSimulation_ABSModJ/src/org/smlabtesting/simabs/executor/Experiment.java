package org.smlabtesting.simabs.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.smlabtesting.simabs.model.SMLabModel;
import org.smlabtesting.simabs.types.SuperConfidenceInterval;
import org.smlabtesting.simabs.variable.Parameters;
import org.smlabtesting.simabs.variable.Seeds;

import cern.jet.random.engine.RandomSeedGenerator;

/**
 * Runs the simulation model. 
 */
public class Experiment {
   public static void main(String[] args) {

	   int NUMRUNS = 3;
	   double confidence = 0.90;
	   
	   // Generate seeds per run.
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       
       Seeds[] sds = new Seeds[NUMRUNS];
       for(int i=0; i<NUMRUNS ; i++) {
    	   sds[i] = new Seeds(rsg);
       }
       
	   double startTime = 0;
	   double endTime = 3600 * 24 * 15;
	   
	   runExperiment(sds, new Parameters(5, new int[]{1,15,15,15,15,15}), startTime, endTime, NUMRUNS, confidence);
	}
	
	public static void runExperiment(Seeds[] seeds, Parameters params, double WARM_UP_END_T, double RUN_END_T, int NUMRUNS, double confidence){
	   double[] percentageLateRegularSamples = new double [NUMRUNS];
	   double[] percentageLateRushSamples = new double [NUMRUNS];
	   int[] missedEntries = new int[] {0,0,0,0,0,0};
	   
	   for(int i = 0; i < NUMRUNS; i++){
		   SMLabModel model = new SMLabModel(WARM_UP_END_T, RUN_END_T, seeds[i], params, false);
		   model.runSimulation();
		   model.output.percentageLateRegularSamples();
		   model.output.percentageLateRushSamples();
		   percentageLateRegularSamples[i] += model.output.percentageLateRegularSamples;
		   percentageLateRushSamples[i] += model.output.percentageLateRushSamples;
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
	   

	   System.out.print("< "+missedEntries[0]);
	   for(int j = 1; j < 6; j++){
		  System.out.print(", " + missedEntries[j]);
	   }
	   System.out.println(" >");
	   System.out.println("Run #\t%LateRegularSample\t%LateRushSample");
	   for(int i = 0; i < NUMRUNS; i++){
		  System.out.println((i+1)+"\t"+percentageLateRegularSamples[i]+"\t"+percentageLateRushSamples[i]);
	   }
	   double avgLateRegSamples = 0;
	   double avgLateRushSamples = 0;
	   
	   for(int i = 0; i < NUMRUNS; i++){
		   avgLateRegSamples += percentageLateRegularSamples[i] / (double) NUMRUNS;
		   avgLateRushSamples += percentageLateRushSamples[i] / (double) NUMRUNS;
	   }
	   
	   SuperConfidenceInterval intervalReg = new SuperConfidenceInterval(percentageLateRegularSamples, confidence);
	   SuperConfidenceInterval intervalRush = new SuperConfidenceInterval(percentageLateRushSamples, confidence);
	   
	   if(avgLateRegSamples <= 0.02 && avgLateRushSamples <= 0.1){
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
	
	public static void getWarmUp(){
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
