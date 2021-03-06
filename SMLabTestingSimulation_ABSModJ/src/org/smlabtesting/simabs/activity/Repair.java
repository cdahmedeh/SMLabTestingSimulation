package org.smlabtesting.simabs.activity;

import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.Idle;
import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.InRepair;
import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.NeedsRepair;

import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalActivity;

/**
 * This activity represents a Test Machine (inside a Test Cell) being repaired after a failure.
 * 
 * Participants: RC.TestingMachine
 * 
 * Thus, there is a separate activity instance for each test machine in every
 * test cell.
 * stationId = one of {C1 = 1, C2 = 2, C3, = 3, C4 = 4, C5 = 5}
 * machineId = from 0 to numCellMachines[stationId]
 */
public class Repair extends ConditionalActivity {
	private SMLabModel model;
	private int stationId;
	private int machineId;

	public Repair(SMLabModel model, int stationId, int machineId) {
		this.model = model;
		this.stationId = stationId;
		this.machineId = machineId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId, int machineId) {
		// The machine has to be ready for repair.
		return model.rcTestingMachine[stationId][machineId].status == NeedsRepair;
	}
	
	@Override
	public void startingEvent() {
		// The machine status becomes in repair.
		model.rcTestingMachine[stationId][machineId].status = InRepair;
	}

	@Override
	protected double duration() {
		// The repair time is defined by a data model that is associated with
		// this specific test cell.
		return model.rvp.uRepairTime(stationId);
	}
	
	@Override
	protected void terminatingEvent() {
		// Generate a new time for the machine to fail.
		// It is initialized at first when the system is created.
		model.rcTestingMachine[stationId][machineId].timeUntilFailure = model.rvp.uFailureDuration(stationId);
		
		// The machine is ready to test again.
        model.rcTestingMachine[stationId][machineId].status = Idle;
	}
}
