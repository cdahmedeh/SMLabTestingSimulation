package simModel;

public class UDPs 
{
	ModelName model;  // for accessing the clock
	
	// Constructor
	public UDPs(ModelName model) { this.model = model; }

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
