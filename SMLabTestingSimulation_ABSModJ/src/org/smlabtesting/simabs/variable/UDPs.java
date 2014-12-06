package org.smlabtesting.simabs.variable;

import static org.smlabtesting.simabs.variable.Constants.STATION_EXITS;
import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;

import org.smlabtesting.simabs.entity.ICSample;
import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

/**
 * UDP.shiftRacetrack() 						is implemented in: RQRacetrack.shiftRacetrack(int)
 * UDP.testsCompleted(sample) 					is implemented in: EnterUnloadBuffer.precondition(...) (see comments there)
 */
public class UDPs {
	@SuppressWarnings("unused")
	private SMLabModel model;
	public UDPs(SMLabModel model) {
		this.model = model; 
	}
	
	public boolean testsCompleted(ICSample sample) {
		return sample.testsRemaining.isEmpty();
	}

	public void completeNextTest(ICSample sample) {
		sample.testsRemaining.pop();
	}

	public boolean testsRemainingNext(ICSample sample, int stationId) {
		return !sample.testsRemaining.isEmpty() && sample.testsRemaining.peek() == stationId;
	}
	
	public void shiftRacetrack(RQRacetrack rqRacetrack) {
		rqRacetrack.slots.offset(1);
	}
	
	public boolean canExitRacetrackQueue(int stationId)
	{
		boolean canExit = model.qRacetrackLine[stationId].n() > 0 
				&& model.rqRacetrack.slots(STATION_EXITS[stationId]) == null;
		
		return canExit;
	}
	
	public boolean canEnterCellBufferQueue(RSampleHolder sampleHolder, int stationId)
	{
		boolean canEnter = model.qTestCellBuffer[stationId].n() < TEST_CELL_BUFFER_CAPACITY
				&& sampleHolder != null 
				&& sampleHolder.sample != null
				&& testsRemainingNext(sampleHolder.sample, stationId);
		
		return canEnter;
	}

}
