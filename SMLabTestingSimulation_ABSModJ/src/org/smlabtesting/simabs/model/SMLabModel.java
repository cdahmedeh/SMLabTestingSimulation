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
import org.smlabtesting.simabs.entity.RCTestingMachine;
import org.smlabtesting.simabs.entity.RLoadUnloadMachine;
import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.variable.DVPs;
import org.smlabtesting.simabs.variable.Output;
import org.smlabtesting.simabs.variable.Parameters;
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
 * x Note that constants are referred through Constants class statically.
 * 
 * In addition, this class instantiates the activities and actions related to 
 * this SM. Finally, it defines the algorithm of preconditions verifications
 * and event scheduling.
 * 
 * This class is a sub-class of AOSimlationModel which handles processing the
 * actual SBL and doing the time advance algorithm.
 * 
 * All times are always, counted in second, and as often as possible, with the
 * decimals truncated.
 */
public class SMLabModel extends AOSimulationModel {
	/* Debug Mode */
	private final boolean debug;
	
	/* Parameter */
	public Parameters parameters;

	/* Entities */
	// The entities are initialized by the SetupSimulation action. The 
	// SampleHolder entities are created in that same action but the Sample 
	// entities are made in the Arrivals activity.
	public RSampleHolder[] rSampleHolders;
	
	// Racetrack related
	public RQRacetrack rqRacetrack;
	public QRacetrackLine[] qRacetrackLine;
	
	// Load/Unload Machine related
	public QNewSamples qNewSamples;
	public QUnloadBuffer qUnloadBuffer;
	public RLoadUnloadMachine rLoadUnloadMachine;
	
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
	public Output output = new Output(this);
	// TODO: Define any methods to read some output.
	
	

	/**
	 * Creates an instance of the SM Testing Lab simulation model.
	 * 
	 * @param t0time  		The start time for the simulation.
	 * @param tftime  		The end time for the simulation. Exclusive.
	 * @param sd 	  		Seeds used to generate the RVPs.
	 * @param parameters	The parameters used to run the simulation with.
	 * @param debug   		Set to true if you want to see the SBL being printed to console.
	 */
	public SMLabModel(double t0time, double tftime, Seeds sd, Parameters parameters, boolean debug) {
		// Setup debug mode.
		this.debug = debug;
		
		// Pass parameters in.		
		this.parameters = parameters;
		
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// Initialize the simulation model
		initAOSimulModel(t0time,tftime);   

		// Prepare the entities by initializing them.
		SetupSimulation init = new SetupSimulation(this);
		scheduleAction(init); 
		
		// Schedule scheduled activities and actions.
		scheduleScheduled();
	}
	
	/**
	 * Schedule all scheduled activities and actions that belong to this 
	 * system.
	 */
	protected void scheduleScheduled() {
		scheduleAction(new Arrival(this));
		scheduleActivity(new RacetrackMove(this));
	}

	/**
	 * Overridden from AOSimulationModel to handle behavior bootstrapping and
	 * to actions/activities missed by the time advance. 
	 */
	@Override
	protected void testPreconditions(Behaviour behObj) {
		// Reschedule any behavior so that its preconditions can be 
		// tested again. Otherwise, repeatable activities are only done once.
		reschedule(behObj);
		
		// Run through preconditions of all conditional activities and
		// conditional actions and start them if needed.
		while(scanPreconditions());

		// No interruptions exist in this model.  
	}
	
	/**
	 * Call this continuously until all preconditions are all false. It will
	 * check all preconditions for all conditional activities and actions, 
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

		// Test Cell activities and actions. There are five of them.
		for (int i = 1; i < 6; i++) {
			// Test cell buffer queue, one per test cell.
			if (EnterTestCellBuffer.precondition(this, i)) {
				EnterTestCellBuffer enterTestCellBuffer = new EnterTestCellBuffer(this, i);
				enterTestCellBuffer.actionEvent();
				preconditions = true;
			}
		
			// There can be multiple testing machines per cell. There is a
			// Repair, Cleaning and Testing activity for each and every one of 
			// them.
			// 
			// It uses the numCellMachines parameter to determine the number
			// of test machines in the system per cell.
			for (int j = 0; j < parameters.numCellMachines[i]; j++) {
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
	 * Overridden to show SBL if debug mode is enabled.
	 */
	@Override
	protected void eventOccured() {
		if (debug) {
			this.showSBL();			
		}
	}
	
	/**
	 * Overriden to show the state of all entities when printing the SBL.
	 */
	@Override
	protected void showSBL() {
		super.showSBL();
		
		System.out.println("-------- Model Information -------- \n");
		System.out
				.print(String.format(
						"Clock: %f, \n"
						+ "Q.NewSamples.n: %d \n"
						+ "\n"
						+ "Station(L/U): \n"
						+ "   Q.UnloadBuffer.n: %d, Q.UnloadBuffer.nEmpty: %d, \n"
						+ "      numFailedStationEntries[0]: %d \n"
						+ "   R.LoadUnloadMachine.busy: %b \n"
						+ "   Q.RacetrackLine[UL].n:  %d \n",
						this.getClock(),
						this.qNewSamples.n(),
						this.qUnloadBuffer.n(), 
						this.qUnloadBuffer.nEmpty,
						this.output.totalFailedStationEntries[0],
						this.rLoadUnloadMachine.busy,
						this.qRacetrackLine[0].n()));
		for (int stationId = 1; stationId < 6; stationId++) {
			System.out.print(String.format(
					"Station(%d): \n"
					+ "   Q.TestCellBuffer.n: %d, totalFailedStationEntries: %d\n"
					,
					stationId,
					this.qTestCellBuffer[stationId].n(),
					this.output.totalFailedStationEntries[stationId])
			);

			for (int machineId = 0; machineId < this.parameters.numCellMachines[stationId]; machineId++) {
				System.out.print(String.format(
					"   RC.TestingMachine[%d][%d].status: %s \n"
					,
					stationId, 
					machineId,
					rcTestingMachine[stationId][machineId].status)
				);
			}

			System.out.println(String.format(
				"   Q.RacetrackLine[%d].n: %d"
				, 
				stationId,
				this.qRacetrackLine[stationId].n())
			);
		}

		System.out.println();
	} 
	
	/**
	 * Overridden and changed visibility to public to be able to retrieve timer
	 * so that scheduled activities don't have to keep their own clock.
	 */
	@Override
	public double getClock() {
		return super.getClock();
	}
}


