package org.smlabtesting.simabs.model;

import org.smlabtesting.simabs.action.Arrival;
import org.smlabtesting.simabs.action.EnterTestCellBuffer;
import org.smlabtesting.simabs.action.EnterUnloadBuffer;
import org.smlabtesting.simabs.action.ExitRacetrackLine;
import org.smlabtesting.simabs.activity.Cleaning;
import org.smlabtesting.simabs.activity.LoadUnloadProcessing;
import org.smlabtesting.simabs.activity.RacetrackMove;
import org.smlabtesting.simabs.activity.Repair;
import org.smlabtesting.simabs.activity.Testing;
import org.smlabtesting.simabs.entity.QNewSamples;
import org.smlabtesting.simabs.entity.QRacetrackLine;
import org.smlabtesting.simabs.entity.QTestCellBuffer;
import org.smlabtesting.simabs.entity.QUnloadBuffer;
import org.smlabtesting.simabs.entity.RCLoadUnloadMachine;
import org.smlabtesting.simabs.entity.RCTestingMachine;
import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.variable.DVPs;
import org.smlabtesting.simabs.variable.Output;
import org.smlabtesting.simabs.variable.RVPs;
import org.smlabtesting.simabs.variable.Seeds;
import org.smlabtesting.simabs.variable.UDPs;

import absmodJ.AOSimulationModel;
import absmodJ.Behaviour;

/**
 * Contains all related variables to a single simulation run. It includes 
 * references to:
 * 
 * - All Entities in the system.
 * - Parameters for the run.
 * - RVPs, UDPs and DVPs.
 * - The Output container.
 * - Input variables, if any.
 * x Note that constants are refered through Constants class statically.
 * 
 * In addition, this class instantiates the activites and actions related to 
 * this SM. Finally, it defines the algorithm of preconditions verifications
 * and event scheduling.
 * 
 * This class is a sub-class of AOSimlationModel which handles processing the
 * actual SBL and doing the time advanced algorithm.
 * 
 */
public class SMLabModel extends AOSimulationModel {
	/* Debug Mode */
	private final boolean debug;
	
	/* Parameter */
	// TODO: Put parameters here.

	/* Entities */
	// The entites are initalized by the SetupSimulation action. The 
	// SampleHolder entities are created in that same action but the Sample 
	// entities are made in the Arrivals activity.
	
	// Racetrack related
	public RQRacetrack rqRacetrack;
	public QRacetrackLine[] qRacetrackLine;
	
	// Load/Unload Machine related
	public QNewSamples qNewSamples;
	public QUnloadBuffer qUnloadBuffer;
	public RCLoadUnloadMachine rcLoadUnloadMachine;
	
	// Testing Cell related
	public QTestCellBuffer[] qTestCellBuffer;
	public RCTestingMachine[][] rcTestingMachine;

	/* Input Variables */
	// TODO: Put inputs here.
	
	/* RVP, DVP and UDP */
	public RVPs rvp;  
	public DVPs dvp = new DVPs(this); 
	public UDPs udp = new UDPs(this);

	/* Output container */
	protected Output output = new Output(this);
	// TODO: Define any methods to read some output.
	
	

	/**
	 * Creates an instance of the SM Testing Lab simulation model.
	 * 
	 * @param t0time  The start time for the simulation.
	 * @param tftime  The end time for the simulation. Exclusive.
	 * @param sd 	  Seeds used to generate the RVPs.
	 * @param debug   Set to true if you want to see the SBL being printed to console.
	 */
	public SMLabModel(double t0time, double tftime, Seeds sd, boolean debug) {
		this.debug = debug;
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// Initialise the simulation model
		initAOSimulModel(t0time,tftime);   

		// Prepare the entities by initalizing them.
		SetupSimulation init = new SetupSimulation(this);
		scheduleAction(init); 
		
		// Schedule scheduled activties and actions.
		scheduleScheduled();
	}
	
	/**
	 * Schedule all scheduled activties and actions that belong to this 
	 * system.
	 */
	protected void scheduleScheduled() {
		scheduleAction(new Arrival(this));
		scheduleActivity(new RacetrackMove(this));
	}

	/**
	 * Overriden from AOSimulationModel to handle behavior bootstrapping and
	 * to actions/activties missed by the time advance. 
	 */
	@Override
	protected void testPreconditions(Behaviour behObj) {
		// Reschedule any behavior so that it's preconditions can be 
		// tested again. Otherwise, repeatable activties are only done once.
		reschedule(behObj);
		
		// Run through preconditions of all conditional activites and
		// conditional actions and start them if needed.
		while(scanPreconditions());

		// No interruptions exist in this model.  
	}
	
	/**
	 * Call this continuiously until all preconditions are all false. It will
	 * check all preconditions for all conditional activties and actions, 
	 * and run an instance of them if the preconditions are met.
	 * 
	 * @return False if all preconditions fail. True if at least one passes.
	 */
	private boolean scanPreconditions() {
		boolean preconditions = false;
		
		// Load/Unload Buffer action.
		if (EnterUnloadBuffer.precondition(this)) {
			EnterUnloadBuffer enterUnloadBuffer = new EnterUnloadBuffer(this);
			enterUnloadBuffer.actionEvent();
			preconditions = true;
		}
		
		// Load/Unload Machine activity.
		if (LoadUnloadProcessing.precondition(this)) {
			LoadUnloadProcessing loadUnloadProcessing = new LoadUnloadProcessing(this);
			loadUnloadProcessing.startingEvent();
			scheduleActivity(loadUnloadProcessing);
			preconditions = true;			
		}

		// Racetrack return queue activity. There is one for all six stations.
		for (int i = 0; i < 6; i++) {
			if (ExitRacetrackLine.precondition(this, i)) {
				ExitRacetrackLine exitRacetrackLine = new ExitRacetrackLine(this, i);
				exitRacetrackLine.actionEvent();
				preconditions = true;				
			}
		}
		
		// Test Cell activties and actions. There are five of them.
		for (int i = 1; i < 6; i++) {
			// Test cell buffer queue, one per test cell.
			if (EnterTestCellBuffer.precondition(this, i)) {
				EnterTestCellBuffer enterTestCellBuffer = new EnterTestCellBuffer(this, i);
				enterTestCellBuffer.actionEvent();
				preconditions = true;
			}

			// There can be multiple testing macines per cell. There is a
			// Repair, Cleaning and Testing activty for each and every one of 
			// them.
			// TODO: Include the parameter for setting the number of testing
			//       machines per cell.
			for (int j = 0; j < 3; j++) {
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
	
	/**
	 * Overriden to show SBL if debug mode is enabled.
	 */
	@Override
	protected void eventOccured() {
		if (debug) {
			this.showSBL();			
		}
	}

	/**
	 * Overriden and changed visibility to public to be able to retrieve timer
	 * so that scheduled activties don't have to keep their own clock.
	 */
	@Override
	public double getClock() {
		return super.getClock();
	}
}


