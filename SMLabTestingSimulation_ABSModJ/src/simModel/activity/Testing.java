package simModel.activity;

import simModel.ModelName;
import simModel.entity.TestingMachine;
import simModel.entity.TestingMachine.TestingMachineState;
import absmodJ.ConditionalActivity;

public class Testing extends ConditionalActivity {

	private ModelName model;
	private int stationId;
	private int machineId;

	public Testing(ModelName model, int stationId, int machineId) {
		this.model = model;
		this.stationId = stationId;
		this.machineId = machineId;
	}
	
	public static boolean precondition(ModelName model, int stationId, int machineId) {
		return model.testingMachine[stationId][machineId].status == TestingMachineState.Idle && (model.testCellBuffer[stationId].hasNext() || model.testingMachine[stationId][machineId].sampleHolder != null);
	}
	
	@Override
	protected double duration() {
		return model.testingMachine[stationId][machineId].runTime;
	}

	@Override
	public void startingEvent() {
		model.testingMachine[stationId][machineId].status = TestingMachineState.Testing;

        if (model.testingMachine[stationId][machineId].sampleHolder == null) {
        	model.testingMachine[stationId][machineId].sampleHolder = model.testCellBuffer[stationId].next();
        }

        int testingTime = (int) model.rvp.generateTestingTime(stationId);

        if (model.testingMachine[stationId][machineId].timeUntilFailure > testingTime) {
        	model.testingMachine[stationId][machineId].runTime = testingTime;
        	model.testingMachine[stationId][machineId].testSuccess = true;
        } else {
        	model.testingMachine[stationId][machineId].runTime = model.testingMachine[stationId][machineId].timeUntilFailure;
        	model.testingMachine[stationId][machineId].testSuccess = false;
        }
	}

	@Override
	protected void terminatingEvent() {
		if (model.testingMachine[stationId][machineId].testSuccess) {
			model.testingMachine[stationId][machineId].sampleHolder.getSample().completedNextTest();
            
            model.racetrackLine[stationId].queue(model.testingMachine[stationId][machineId].sampleHolder);
            model.testingMachine[stationId][machineId].sampleHolder = null;
            model.testingMachine[stationId][machineId].status = TestingMachineState.Idle;
            model.testingMachine[stationId][machineId].completedTests++;
            
            if (stationId == 2 && (model.testingMachine[stationId][machineId].completedTests % TestingMachine.STATION_2_CLEANING_THRESHOLD == 0)) {
            	model.testingMachine[stationId][machineId].status = TestingMachineState.Cleaning;
            }
        } else {
        	model.testingMachine[stationId][machineId].status = TestingMachineState.Repair;
        }
	}
}
