package org.smlabtesting.simabs.activity;

import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.InCleaning;
import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.Idle;
import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.NeedsCleaning;

import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalActivity;

/**
 * This activity represents a Test Machine (inside a Test Cell) being cleaned
 * after it exceeds its cleaning threshold.
 * 
 * Participants: R.TestingMachine
 * 
 * There is a separate activity instance for each test machine in every
 * test cell. 
 * 
 * In the CM, this activity can only be created for cell C2. This was done 
 * differently here just to be consistent with Repair and Testing activity.
 * 
 * stationId = one of {C1 = 1, C2 = 2, C3, = 3, C4 = 4, C5 = 5}
 * machineId = from 0 to numCellMachines[stationId]
 */
public class Cleaning extends ConditionalActivity {
	private SMLabModel model;
	private int stationId;
	private int machineId;

	public Cleaning(SMLabModel model, int stationId, int machineId) {
		this.model = model;
		this.stationId = stationId;
		this.machineId = machineId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId, int machineId) {
		// The machine has to be ready for cleaning.
		return model.rcTestingMachine[stationId][machineId].status == NeedsCleaning;
	}
	
	@Override
	public void startingEvent() {
		// The machine status becomes in cleaning.
		model.rcTestingMachine[stationId][machineId].status = InCleaning;
	}

	@Override
	protected double duration() {
		// The cleaning time is defined by a data model that is associated
		// with a specific test cell. It is only defined for C2.
		return model.rvp.uCleaningTime();
	}
	
	@Override
	protected void terminatingEvent() {
		// The machine is ready to test again.
        model.rcTestingMachine[stationId][machineId].status = Idle;
	}
}
