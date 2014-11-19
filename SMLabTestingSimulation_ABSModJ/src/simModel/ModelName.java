package simModel;

import simModel.action.Arrival;
import simModel.action.EnterTestCellBuffer;
import simModel.action.EnterUnloadBuffer;
import simModel.action.ExitRacetrackLine;
import simModel.action.SetupSimulation;
import simModel.activity.Cleaning;
import simModel.activity.LoadUnloadProcessing;
import simModel.activity.RacetrackMove;
import simModel.activity.Repair;
import simModel.activity.Testing;
import simModel.entity.LoadUnloadMachine;
import simModel.entity.NewSamples;
import simModel.entity.Racetrack;
import simModel.entity.RacetrackLine;
import simModel.entity.TestCellBuffer;
import simModel.entity.TestingMachine;
import simModel.entity.UnloadBuffer;
import absmodJ.AOSimulationModel;
import absmodJ.Behaviour;
import absmodJ.SequelActivity;
//
// The Simulation model Class
public class ModelName extends AOSimulationModel
{
	// Constants available from Constants class
	/* Parameter */
        // Define the parameters

	/*-------------Entity Data Structures-------------------*/
	public Racetrack racetrack;
	public NewSamples newSamples;
	public UnloadBuffer unloadBuffer;
	public RacetrackLine[] racetrackLine;
	public LoadUnloadMachine loadUnloadMachine;
	public TestCellBuffer[] testCellBuffer;
	public TestingMachine[][] testingMachine;
	/* Group and Queue Entities */
	// Define the reference variables to the various 
	// entities with scope Set and Unary
	// Objects can be created here or in the Initialise Action

	/* Input Variables */
	// Define any Independent Input Varaibles here
	
	// References to RVP and DVP objects
	public RVPs rvp;  // Reference to rvp object - object created in constructor
	public DVPs dvp = new DVPs(this);  // Reference to dvp object
	public UDPs udp = new UDPs(this);

	// Output object
	protected Output output = new Output(this);

	
	// Output values - define the public methods that return values
	// required for experimentation.


	// Constructor
	public ModelName(double t0time, double tftime, /*define other args,*/ Seeds sd)
	{
		// Initialise parameters here
		
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// rgCounter and qCustLine objects created in Initalise Action
		
		// Initialise the simulation model
		initAOSimulModel(t0time,tftime);   

		     // Schedule the first arrivals and employee scheduling
		SetupSimulation init = new SetupSimulation(this);
		scheduleAction(init);  // Should always be first one scheduled.
		// Schedule other scheduled actions and acitvities here
		// TODO:
		scheduleAction(new Arrival(this));
		scheduleActivity(new RacetrackMove(this));
	}

	/************  Implementation of Data Modules***********/	
	/*
	 * Testing preconditions
	 */
	protected void testPreconditions(Behaviour behObj)
	{
		// TODO: Allow bootstrapping
		reschedule(behObj);
		
		// Check preconditions of Conditional Activities
		while(scanPreconditions());

		// Check preconditions of Interruptions in Extended Activities
	}
	
	private boolean scanPreconditions() {
		boolean preconditions = false;
		
		if (EnterUnloadBuffer.precondition(this)) {
			EnterUnloadBuffer enterUnloadBuffer = new EnterUnloadBuffer(this);
			enterUnloadBuffer.actionEvent();
			preconditions = true;
		}
		
		if (LoadUnloadProcessing.precondition(this)) {
			LoadUnloadProcessing loadUnloadProcessing = new LoadUnloadProcessing(this);
			loadUnloadProcessing.startingEvent();
			scheduleActivity(loadUnloadProcessing);
			preconditions = true;			
		}

		for (int i = 0; i < 6; i++) {
			if (ExitRacetrackLine.precondition(this, i)) {
				ExitRacetrackLine exitRacetrackLine = new ExitRacetrackLine(this, i);
				exitRacetrackLine.actionEvent();
				preconditions = true;				
			}
		}
		
		for (int i = 1; i < 6; i++) {
			if (EnterTestCellBuffer.precondition(this, i)) {
				EnterUnloadBuffer enterUnloadBuffer = new EnterUnloadBuffer(this);
				enterUnloadBuffer.actionEvent();
				preconditions = true;
			}

			for (int j = 0; i < 3; i++) {
				if (Testing.precondition(this, i, j)) {
					Testing testing = new Testing(this, i, j);
					testing.startingEvent();
					scheduleActivity(testing);
					preconditions = true;	
				}
				
				if (Repair.precondition(this, i, j)) {
					Repair repair = new Repair(this, i, j);
					repair.startingEvent();
					scheduleActivity(repair);
					preconditions = true;	
				}
				
				if (Cleaning.precondition(this, i, j)) {
					Cleaning cleaning = new Cleaning(this, i, j);
					cleaning.startingEvent();
					scheduleActivity(cleaning);
					preconditions = true;	
				}
			}
		}
		
		return preconditions;
	}
	
	protected void eventOccured()
	{
		this.showSBL(); //TODO: Needs condition......
		// Can add other debug code to monitor the status of the system
		// See examples for suggestions on setup logging

		// Setup an updateTrjSequences() method in the Output class
		// and call here if you have Trajectory Sets
		// updateTrjSequences() 
	}

	// Standard Procedure to start Sequel Activities with no parameters
	protected void spStart(SequelActivity seqAct)
	{
		seqAct.startingEvent();
		scheduleActivity(seqAct);
	}	
	public double getClock() {return super.getClock();}
}


