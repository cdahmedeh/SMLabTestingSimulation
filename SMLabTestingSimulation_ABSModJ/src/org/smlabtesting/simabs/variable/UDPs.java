package org.smlabtesting.simabs.variable;

import java.util.stream.DoubleStream;

import org.smlabtesting.simabs.model.SMLabModel;

public class UDPs 
{
	SMLabModel model;  // for accessing the clock
	
	// TODO: -- OUR STUFF
    private static final double[] TESTING_CYCLE_TIMES = DoubleStream.of(0, 0.77, 0.85 , 1.04, 1.24 , 1.7).map(i -> i * 60).toArray();
    public double uTestingTime(int stationId) {
        return TESTING_CYCLE_TIMES[stationId];
    }
	// TODO: -- OUR STUFF
	
	// Constructor
	public UDPs(SMLabModel model) { this.model = model; }

	// Translate User Defined Procedures into methods
    /*-------------------------------------------------
	                       Example
	                       public int ClerkReadyToCheckOut()
        {
        	int num = 0;
        	Clerk checker;
        	while(num < model.NumClerks)
        	{
        		checker = model.Clerks[num];
        		if((checker.currentstatus == Clerk.status.READYCHECKOUT)  && checker.list.size() != 0)
        		{return num;}
        		num +=1;
        	}
        	return -1;
        }
	------------------------------------------------------------*/
	
	
}
