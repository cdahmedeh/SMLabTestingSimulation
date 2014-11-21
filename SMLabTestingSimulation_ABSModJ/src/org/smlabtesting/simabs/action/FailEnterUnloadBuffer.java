package org.smlabtesting.simabs.action;

import static org.smlabtesting.simabs.variable.Constants.STATION_ENTRANCES;
import static org.smlabtesting.simabs.variable.Constants.UNLOADBUFFER_CAPACITY;

import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalAction;

/**
 * This action describes when a Sample Holder tries to exits the Racetrack to go into 
 * the load unload station's unload buffer but cannot do so because the buffer is already full. 
 * This happens when the sample has completed testing and is ready to be unloaded from the SUI. 
 * The output variable totalFailedStationEntries for the cell is incremented.
 * 
 * Participants: Q.UnloadBuffer 
 * Uses: RQ.Racetrack, R.SampleHolder (implicit), iC.Sample (implicit)
 */
public class FailEnterUnloadBuffer extends ConditionalAction {
	private SMLabModel model;
	private int stationId = 0;

	public FailEnterUnloadBuffer(SMLabModel model) {
		this.model = model;
	}
	
	public static boolean precondition(SMLabModel model) {
		int stationId = 0;
		// Used to point to the holder that is at the test cell buffer 
		// entrance point. Does not exist in CM.
		RSampleHolder sampleHolder = model.rqRacetrack.slots(STATION_ENTRANCES[stationId]);
		
		// First check that here is a holder at the entrance point of the test 
		// cell buffer. Then check if that holder has the current test cell 
		// as next in its sequence. Also check for vacancy in the test cell buffer.
        return model.qUnloadBuffer.n() == UNLOADBUFFER_CAPACITY
                && sampleHolder != null
                && sampleHolder.sample != null
                && sampleHolder.sample.testsRemainingNext(stationId) == false; // sample.testsRemaining.next = CX
	}
	
	@Override
	public void actionEvent() {
        //Then increment the number of times samples have failed to enter the unload station. 
		model.output.totalFailedStationEntries[stationId]++;
	}
}