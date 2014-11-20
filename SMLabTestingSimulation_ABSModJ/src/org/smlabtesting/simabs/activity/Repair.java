package org.smlabtesting.simabs.activity;

import org.smlabtesting.simabs.entity.RCTestingMachine.TestingMachineState;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalActivity;

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
		return model.rcTestingMachine[stationId][machineId].status == TestingMachineState.Repair;
	}
	
	@Override
	protected double duration() {
		return model.rvp.generateRepairTime(stationId);
	}

	@Override
	public void startingEvent() {
		//TODO: WHY DO WE NEED TO CHECK PRECONDITIONS SO WE CAN DO THEM AGAIN????!?!?!
		model.rcTestingMachine[stationId][machineId].status = TestingMachineState.InRepair;
		
	}

	@Override
	protected void terminatingEvent() {
		model.rcTestingMachine[stationId][machineId].timeUntilFailure = (int) model.rvp.generateFailureTime(stationId);
        model.rcTestingMachine[stationId][machineId].status = TestingMachineState.Idle;
	}
}