package org.smlabtesting.simabs.activity;

import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.Idle;
import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.NeedsCleaning;
import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.NeedsRepair;
import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.Testing;

import org.smlabtesting.simabs.entity.RCTestingMachine;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalActivity;

/**
 * This activity represents a Test Machine (inside a Test Cell) performing a
 * test on a sample.
 * 
 * Participants: RC.TestingMachine[stationId][machineId]
 * Uses: Q.TestCellBuffer, Q.RacetrackLine,
 *       R.SampleHolder (implicit), iC.Sample (implicit)
 * 
 * There are five instances in Q.TestCellBuffer and RC.TestingMachine. 
 * Instances are grouped by the same identifier machineId = X. Then per 
 * test cell, there is another identifier per test machine.
 * 
 * Thus, there is a separate activity instance for each test machine in every
 * test cell.
 * stationId = one of {C1 = 1, C2 = 2, C3, = 3, C4 = 4, C5 = 5}
 * machineId = from 0 to numCellMachines[stationId]
 */
public class Testing extends ConditionalActivity {
	private SMLabModel model;
	private int stationId;
	private int machineId;

	public Testing(SMLabModel model, int stationId, int machineId) {
		this.model = model;
		this.stationId = stationId;
		this.machineId = machineId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId, int machineId) {
		// The machine has to be idle, and there has to be either a holder
		// waiting in line to be tested or a holder already in the machine 
		// from a previous failure.
		return model.rcTestingMachine[stationId][machineId].status == Idle && 
				(model.qTestCellBuffer[stationId].n() > 0 
				|| model.rcTestingMachine[stationId][machineId].sampleHolder != null);
	}
	
	@Override
	public void startingEvent() {
		// Refers to the testing machine that is processed by this activity. 
		// Does not exist in the CM, just for code clarity.
		RCTestingMachine testingMachine = model.rcTestingMachine[stationId][machineId];
		
		// Machine becomes busy
		testingMachine.status = Testing;

		// If there a holder in queue for testing and there is no holder 
		// already in the machine from a previous failure, then insert the
		// waiting holder into the machine
        if (testingMachine.sampleHolder == null) {
        	testingMachine.sampleHolder = model.qTestCellBuffer[stationId].removeQue();
        }
        // Otherwise, there is a sample holder already in the machine. So it
        // will be retested.

        // We want to avoid counting the runtime at every single second in 
        // order to determine when the machine will fail, instead it is
        // predicted.

        // First, check how much time line until the next failure and
        // compare it against the runtime.
        
        int testingTime = model.dvp.uTestingTime(stationId);

        if (testingMachine.timeUntilFailure > testingTime) {
        	// If the test runs longer than time until failure, then test will not fail.
        	testingMachine.runTime = testingTime;
        	testingMachine.testSuccess = true;
        } else {
        	// Otherwise, the remaining time until failure is the machine runtime
        	testingMachine.runTime = testingMachine.timeUntilFailure;
        	testingMachine.testSuccess = false;
        }
	}

	@Override
	protected double duration() {
		// Simulate cell testing time. It is either the test runtime, or time 
		// until the failure.
		return model.rcTestingMachine[stationId][machineId].runTime;
	}
	
	@Override
	protected void terminatingEvent() {
		// Refers to the testing machine that is processed by this activity. 
		// Does not exist in the CM, just for code clarity.
		RCTestingMachine testingMachine = model.rcTestingMachine[stationId][machineId];

		if (testingMachine.testSuccess) {
			// Remove one entry from the upcoming test sequence list.
			model.udp.completeNextTest(testingMachine.sampleHolder.sample);
            
			// Put the tested holder back inline to return to the racetrack only if the test succeeded.
            model.qRacetrackLine[stationId].insertQue(testingMachine.sampleHolder);
            testingMachine.sampleHolder = null;
            testingMachine.status = Idle;
            
            // Subtract the machine runtime from the failure time. Except for
            // test cell number 2 that never fails.
            if (stationId != 2) {
                testingMachine.timeUntilFailure -= testingMachine.runTime;            	
            }
            
            // Count the number of completed tests.
            testingMachine.completedTests++;
            
            // Cell C2 needs to be cleaned after 300 tests.
            if (stationId == 2 && (testingMachine.completedTests % 300 == 0)) {
            	testingMachine.status = NeedsCleaning;
            }
        } else {
        	// Otherwise, the test failed. That means the holder stays in the 
        	// machine and machine goes for repair
        	testingMachine.status = NeedsRepair;
        }
	}
}
