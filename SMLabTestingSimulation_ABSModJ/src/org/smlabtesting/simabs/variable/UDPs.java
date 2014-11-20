package org.smlabtesting.simabs.variable;

import org.smlabtesting.simabs.model.SMLabModel;

public class UDPs 
{
	SMLabModel model;  // for accessing the clock
	
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