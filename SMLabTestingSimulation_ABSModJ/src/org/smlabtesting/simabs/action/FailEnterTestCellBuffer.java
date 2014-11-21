package org.smlabtesting.simabs.action;

import static org.smlabtesting.simabs.variable.Constants.STATION_ENTRANCES;
import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;

import org.smlabtesting.simabs.entity.ICSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalAction;

/**
 * This action describes when a Sample Holder tries to exits the Racetrack to go into 
 * a Test Cell buffer but cannot do so because the Test Cell buffer is already full. This happens when the incoming sample has the current 
 * cell as the next test in its sequence. The output variable totalFailedStationEntries for the cell is incremented.
 * 
 * Participants: Q.TestCellBuffer
 * Uses: RQ.Racetrack, iC.SampleHolder (implicit), iC.Sample (implicit)

 * There are five instances in Q.TestCellBuffer, one per test cell. There is 
 * a separate action for each one.
 * stationId = one of {C1 = 1, C2 = 2, C3, = 3, C4 = 4, C5 = 5}
 */
public class FailEnterTestCellBuffer extends ConditionalAction {
	private SMLabModel model;
	private int stationId;

	public FailEnterTestCellBuffer(SMLabModel model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId) {
		// Used to point to the holder that is at the test cell buffer 
		// entrance point. Does not exist in CM.
		ICSampleHolder holder = model.rqRacetrack.slots(STATION_ENTRANCES[stationId]);
		
		// First check that here is a holder at the entrance point of the test 
		// cell buffer. Then check if that holder has the current test cell 
		// as next in its sequence. Also check for vacancy in the test cell buffer.
        return model.qTestCellBuffer[stationId].n() == TEST_CELL_BUFFER_CAPACITY
                && holder != null
                && holder.sample != null
                && holder.sample.testsRemainingNext(stationId); // sample.testsRemaining.next = CX
	}
	
	@Override
	public void actionEvent() {
        //Then increment the number of times samples have failed to enter the unload station. 
		model.output.totalFailedStationEntries[stationId]++;
	}
}