package simModel.activity;

import simModel.ModelName;
import simModel.entity.TestingMachine.TestingMachineState;
import absmodJ.ConditionalActivity;

public class Repair extends ConditionalActivity {

	private ModelName model;
	private int stationId;
	private int machineId;

	public Repair(ModelName model, int stationId, int machineId) {
		this.model = model;
		this.stationId = stationId;
		this.machineId = machineId;
	}
	
	public static boolean precondition(ModelName model, int stationId, int machineId) {
		return model.testingMachine[stationId][machineId].status == TestingMachineState.Repair;
	}
	
	@Override
	protected double duration() {
		return model.rvp.generateRepairTime(stationId);
	}

	@Override
	public void startingEvent() {
		//TODO: WHY DO WE NEED TO CHECK PRECONDITIONS SO WE CAN DO THEM AGAIN????!?!?!
		model.testingMachine[stationId][machineId].status = TestingMachineState.InRepair;
		
	}

	@Override
	protected void terminatingEvent() {
		model.testingMachine[stationId][machineId].timeUntilFailure = (int) model.rvp.generateFailureTime(stationId);
        model.testingMachine[stationId][machineId].status = TestingMachineState.Idle;
	}
}
