package simModel;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;

class RVPs 
{
	ModelName model; // for accessing the clock
    // Data Models - i.e. random veriate generators for distributions
	// are created using Colt classes, define 
	// reference variables here and create the objects in the
	// constructor with seeds

	/* Data Models for implementing timing maps */
	protected Exponential interArrDist;  // Exponential distribution for interarrival times
	final double WMEAN1=10.0;

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
