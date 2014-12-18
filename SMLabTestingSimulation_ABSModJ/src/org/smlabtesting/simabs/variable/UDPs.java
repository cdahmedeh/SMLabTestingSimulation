package org.smlabtesting.simabs.variable;

import static org.smlabtesting.simabs.variable.Constants.STATION_EXITS;
import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;

import org.smlabtesting.simabs.entity.ICSample;
import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

/**
 * UDP.shiftRacetrack() 						is implemented in: RQRacetrack.shiftRacetrack(int)
 * UDP.testsRemainingNext(sample, stationId)	is implemented in: EnterTestCellBuffer.precondition(...) (see comments there)
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
		if(sample == null)
			return false;
		
		return !sample.testsRemaining.isEmpty() && sample.testsRemaining.peek() == stationId;
	}
	
	public void shiftRacetrack(RQRacetrack rqRacetrack) {
		rqRacetrack.slots.offset(1);
	}
	
	public RSampleHolder getSampleHolder(Integer id){
		if(id == null)
			return null;
		
		RSampleHolder sampleHolder = model.sampleHolders[id];
		
		if(sampleHolder.id != id)
			throw new IllegalArgumentException("Requested id value mismatch with sampleHolders array order");
		
		return sampleHolder;
	}
	
		
	public boolean canExitRacetrackQueue(int stationId) {
		boolean canExit = model.qRacetrackLine[stationId].n() > 0 
				&& model.rqRacetrack.slots(STATION_EXITS[stationId]) == null;
		
		return canExit;
	}
	
	public boolean canEnterCellBufferQueue(RSampleHolder sampleHolder, int stationId) {
		boolean canEnter = model.qTestCellBuffer[stationId].n() < TEST_CELL_BUFFER_CAPACITY
				&& sampleHolder != null 
				&& sampleHolder.sample != null
				&& testsRemainingNext(sampleHolder.sample, stationId);
		
		// Check for the case when a sample holder would have needed to enter a station, 
		// but could not due to the qTestCellBuffer being full. Increment output variable.
		if(!canEnter && sampleHolder != null && testsRemainingNext(sampleHolder.sample, stationId)){
			model.output.totalFailedStationEntries[stationId]++;
		}
		
		return canEnter;
	}

	/**
	 * Called when a sample is exiting the SUI, takes care of incrementing the appropriate output variables
	 * @param sample The sample that is exiting the SUI
	 */
	public void sampleFinished(ICSample sample) {
		if(sample.rush){
			model.output.totalNumRushSamples++;
			if((model.getClock() - sample.startTime) > Constants.RUSH_SAMPLE_MAX_TIME){
				 model.output.lateRushSamples++;
			}
		} else {
			model.output.totalNumRegularSamples++;
			if((model.getClock() - sample.startTime) > Constants.REGULAR_SAMPLE_MAX_TIME){
				 model.output.lateRegularSamples++;
			}
		}
	}
}
