package simModel.activity;

import simModel.ModelName;
import simModel.entity.RCTestingMachine;
import simModel.entity.RCTestingMachine.TestingMachineState;
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
		return model.rcTestingMachine[stationId][machineId].status == TestingMachineState.Idle && (model.qTestCellBuffer[stationId].hasNext() || model.rcTestingMachine[stationId][machineId].icSampleHolder != null);
	}
	
	@Override
	protected double duration() {
		return model.rcTestingMachine[stationId][machineId].runTime;
	}

	@Override
	public void startingEvent() {
		model.rcTestingMachine[stationId][machineId].status = TestingMachineState.Testing;

        if (model.rcTestingMachine[stationId][machineId].icSampleHolder == null) {
        	model.rcTestingMachine[stationId][machineId].icSampleHolder = model.qTestCellBuffer[stationId].removeQue();
        }

        int testingTime = (int) model.rvp.generateTestingTime(stationId);

        if (model.rcTestingMachine[stationId][machineId].timeUntilFailure > testingTime) {
        	model.rcTestingMachine[stationId][machineId].runTime = testingTime;
        	model.rcTestingMachine[stationId][machineId].testSuccess = true;
        } else {
        	model.rcTestingMachine[stationId][machineId].runTime = model.rcTestingMachine[stationId][machineId].timeUntilFailure;
        	model.rcTestingMachine[stationId][machineId].testSuccess = false;
        }
	}

	@Override
	protected void terminatingEvent() {
		if (model.rcTestingMachine[stationId][machineId].testSuccess) {
			model.rcTestingMachine[stationId][machineId].icSampleHolder.getSample().completedNextTest();
            
            model.qRacetrackLine[stationId].insertQue(model.rcTestingMachine[stationId][machineId].icSampleHolder);
            model.rcTestingMachine[stationId][machineId].icSampleHolder = null;
            model.rcTestingMachine[stationId][machineId].status = TestingMachineState.Idle;
            model.rcTestingMachine[stationId][machineId].completedTests++;
            
            if (stationId == 2 && (model.rcTestingMachine[stationId][machineId].completedTests % RCTestingMachine.STATION_2_CLEANING_THRESHOLD == 0)) {
            	model.rcTestingMachine[stationId][machineId].status = TestingMachineState.Cleaning;
            }
        } else {
        	model.rcTestingMachine[stationId][machineId].status = TestingMachineState.Repair;
        }
	}
}
