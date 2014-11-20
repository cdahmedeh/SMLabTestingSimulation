package org.smlabtesting.simabs.activity;

import org.smlabtesting.simabs.entity.RCTestingMachine;
import org.smlabtesting.simabs.entity.RCTestingMachine.TestingMachineState;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalActivity;

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
		return model.rcTestingMachine[stationId][machineId].status == TestingMachineState.Cleaning;
	}
	
	@Override
	protected double duration() {
		return model.rvp.generateCleaningTime();
	}

	@Override
	public void startingEvent() {
		//TODO: WHY DO WE NEED TO CHECK PRECONDITIONS SO WE CAN DO THEM AGAIN????!?!?!
		model.rcTestingMachine[stationId][machineId].status = TestingMachineState.InCleaning;
	}

	@Override
	protected void terminatingEvent() {
        model.rcTestingMachine[stationId][machineId].status = TestingMachineState.Idle;
	}
}
