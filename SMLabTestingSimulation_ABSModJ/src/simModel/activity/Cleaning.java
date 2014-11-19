package simModel.activity;

import simModel.ModelName;
import simModel.entity.TestingMachine;
import simModel.entity.TestingMachine.TestingMachineState;
import absmodJ.ConditionalActivity;

public class Cleaning extends ConditionalActivity {

	private ModelName model;
	private int stationId;
	private int machineId;

	public Cleaning(ModelName model, int stationId, int machineId) {
		this.model = model;
		this.stationId = stationId;
		this.machineId = machineId;
	}
	
	public static boolean precondition(ModelName model, int stationId, int machineId) {
		return model.testingMachine[stationId][machineId].status == TestingMachineState.Cleaning;
	}
	
	@Override
	protected double duration() {
		return model.rvp.generateCleaningTime();
	}

	@Override
	public void startingEvent() {
	}

	@Override
	protected void terminatingEvent() {
        model.testingMachine[stationId][machineId].status = TestingMachineState.Idle;
	}
}
