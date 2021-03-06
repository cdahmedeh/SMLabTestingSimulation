package org.smlabtesting.simabs.action;

import static org.smlabtesting.simabs.variable.Constants.STATION_ENTRANCES;

import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalAction;

/**
 * This action describes when a Sample Holder exits the Racetrack to go into 
 * a Test Cell buffer. This happens when the incoming sample has the current 
 * cell as the next test in its sequence.
 * 
 * Participants: Q.TestCellBuffer
 * Uses: RQ.Racetrack, R.SampleHolder (implicit), iC.Sample (implicit)

 * There are five instances in Q.TestCellBuffer, one per test cell. There is 
 * a separate action for each one.
 * stationId = one of {C1 = 1, C2 = 2, C3, = 3, C4 = 4, C5 = 5}
 */
public class EnterTestCellBuffer extends ConditionalAction {
	private SMLabModel model;
	private int stationId;

	public EnterTestCellBuffer(SMLabModel model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId) {
		// References the holder that is at the test cell buffer entrance point.
		Integer sampleHolderId = model.rqRacetrack.slots(STATION_ENTRANCES[stationId]);
		RSampleHolder sampleHolder = model.udp.getSampleHolder(sampleHolderId);

		// First check that here is a holder at the entrance point of the test 
		// cell buffer. Then check if that holder has the current test cell 
		// as next in its sequence. Also check for vacancy in the test cell buffer.
		return model.udp.canEnterCellBufferQueue(sampleHolder, stationId);
	}
	
	@Override
	public void actionEvent() {
        // Move the sample from the racetrack to the test cell buffer queue.
        Integer sampleHolderId = model.rqRacetrack.slots(STATION_ENTRANCES[stationId]);
		RSampleHolder sampleHolder = model.udp.getSampleHolder(sampleHolderId);
        model.rqRacetrack.setSlot(STATION_ENTRANCES[stationId], null);
        model.qTestCellBuffer[stationId].insertQue(sampleHolder.id);        	
	}
}

