package org.smlabtesting.simabs.executor;
// File: Experiment.java
// Description:

import org.smlabtesting.simabs.model.SMLabModel;
import org.smlabtesting.simabs.variable.Seeds;

import cern.jet.random.engine.RandomSeedGenerator;

// Main Method: Experiments
// 
class Experiment
{
   public static void main(String[] args)
   {
       int i, NUMRUNS = 1; 
       double startTime=0.0, endTime=30*24*3600;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMLabModel mname;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
       // Loop for NUMRUN simulation runs for each case
       // Case 1
       long start = System.currentTimeMillis();
       
       System.out.println(" Case 1");
       for(i=0 ; i < NUMRUNS ; i++)
       {
          mname = new SMLabModel(startTime,endTime,sds[i], true);
          mname.runSimulation();
          // See examples for hints on collecting output
          // and developping code for analysis
       }
       
       long end = System.currentTimeMillis();
       System.out.println(end - start);
   }
}
