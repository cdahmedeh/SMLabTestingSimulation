package org.smlabtesting.simabs.variable;

import static org.smlabtesting.simabs.variable.Constants.STATION_ENTRANCES;
import static org.smlabtesting.simabs.variable.Constants.STATION_EXITS;
import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;
import static org.smlabtesting.simabs.variable.Constants.UNLOADBUFFER_CAPACITY;

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
		if(id == null) {
			return null;
		}
			
		RSampleHolder sampleHolder = model.sampleHolders[id];
		
		if (sampleHolder == null) {
			throw new NullPointerException("The request sample holder does not exist.");
		}
		
		if(sampleHolder.id != id) {
			throw new IllegalArgumentException("Requested id value mismatch with sampleHolders array order");
		}

		return sampleHolder;
	}
	
		
	public boolean canExitRacetrackQueue(int stationId) {
		// First check that here is a holder inline to merge onto the track and 
		// then check if the exit point of this queue onto the racetrack is
		// actually vacant.
		boolean canExit = model.qRacetrackLine[stationId].n() > 0 
				&& model.rqRacetrack.slots(STATION_EXITS[stationId]) == null;
		
		return canExit;
	}

	// First check that here is a holder at the entrance point of the test 
	// cell buffer. Then check if that holder has the current test cell 
	// as next in its sequence. Also check for vacancy in the test cell buffer.
	public boolean canEnterCellBufferQueue(RSampleHolder sampleHolder, int stationId) {
		boolean canEnter = model.qTestCellBuffer[stationId].n() < TEST_CELL_BUFFER_CAPACITY
				&& sampleHolder != null 
				&& sampleHolder.sample != null
				&& testsRemainingNext(sampleHolder.sample, stationId);
		
		return canEnter;
	}

	public boolean canEnterUnloadBuffer(RSampleHolder sampleHolder) {
		// Check that there is actually a holder in the entrance point of the
		// load/unload buffer and that the buffer is not full. Note: the unload 
		// buffer has a length of 5.
		
		return ( sampleHolder != null && model.qUnloadBuffer.n() < UNLOADBUFFER_CAPACITY ) 
                && (
                		// Either there is a sample that has completed all tests
                		// and it can always go in.
                        sampleHolder.sample != null  
                        && model.udp.testsCompleted(sampleHolder.sample)
                        
                        || /* OR */
                        
                        // Or there is an empty sample holder and that the 
                        // number of reserved buffer spots for completed samples
                        // is still respected.
                        sampleHolder.sample == null
                        && model.qUnloadBuffer.nEmpty < model.parameters.maxEmptyHolders
                   );
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
	
	public void updateMissedCounts() {
		//Handle the missed counts for all the machines.
		
		RSampleHolder sampleHolder = model.udp.getSampleHolder(model.rqRacetrack.slots(STATION_ENTRANCES[0]));
	
		// First check that here is a holder at the entrance point of the test 
		// cell buffer. Then check if that holder has the current test cell 
		// as next in its sequence. And then check if the unload buffer is full.
        if (model.qUnloadBuffer.n() == UNLOADBUFFER_CAPACITY
                && sampleHolder != null
                && sampleHolder.sample != null
                && model.udp.testsCompleted(sampleHolder.sample)) {
        	model.output.totalFailedStationEntries[0]++;
        }

        // Handle the missed counts for the test cells.
        for (int stationId = 1; stationId < 6; stationId++) {
            // Used to point to the holder that is at the test cell buffer 
     		// entrance point. Does not exist in CM.
           	RSampleHolder sampleHolder_ = model.udp.getSampleHolder(model.rqRacetrack.slots(STATION_ENTRANCES[stationId]));
         		
     		// First check that here is a holder at the entrance point of the test 
     		// cell buffer. Then check if that holder has the current test cell 
     		// as next in its sequence. And then check if the test cell buffer is full.
             if (model.qTestCellBuffer[stationId].n() == TEST_CELL_BUFFER_CAPACITY
                     && sampleHolder_ != null
                     && sampleHolder_.sample != null
                     && model.udp.testsRemainingNext(sampleHolder_.sample, stationId)) {
            	 model.output.totalFailedStationEntries[stationId]++;
             }
        }
	}
}
