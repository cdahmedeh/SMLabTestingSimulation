package org.smlabtesting.simabs.action;

import static org.smlabtesting.simabs.variable.Constants.STATION_ENTRANCES;
import static org.smlabtesting.simabs.variable.Constants.UNLOADBUFFER_CAPACITY;

import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalAction;

/**
 * This action describes when a Sample Holder exits the Racetrack to go onto 
 * the UnloadBuffer. This happens when a sample has gone through all the tests 
 * and is ready to leave the system or when a sample holder is empty.
 *
 * Participants: Q.UnloadBuffer
 * Uses: Q.Racetrack, R.SampleHolder (implicit), iC.Sample (implicit)
 */
public class EnterUnloadBuffer extends ConditionalAction {
	private SMLabModel model;

	public EnterUnloadBuffer(SMLabModel model) {
		this.model = model;
	}
	
	public static boolean precondition(SMLabModel model) {
		// Used to point to the holder that is at the load/unload buffer 
		// entrance point. Does not exist in CM.
		Integer sampleHolderId = model.rqRacetrack.slots(STATION_ENTRANCES[0]);
		RSampleHolder sampleHolder = model.udp.getSampleHolder(sampleHolderId);
        
		if( sampleHolder == null ) { //TODO: Consider removal.
			return false;
		}
			
		// Check that there is actually a holder in the entrance point of the
		// load/unload buffer and that the buffer is not full. Note: the unload 
		// buffer has a length of 5.
		return model.udp.canEnterUnloadBuffer(sampleHolder);
	}
	
	@Override
	public void actionEvent() {
		// In the CM, this is declared later.
		Integer sampleHolderId = model.rqRacetrack.slots(STATION_ENTRANCES[0]);
		RSampleHolder sampleHolder = model.udp.getSampleHolder(sampleHolderId);
		
		if( sampleHolder == null ) { //TODO: Consider removal.
			return;
		}
		
		// If the sample holder coming in has no sample, then increment the
		// empty sample holder counter.
        if(sampleHolder.sample == null) {
        	model.qUnloadBuffer.nEmpty++;
        }
		
        // Move the sample holder from the racetrack to the unload buffer queue.
        model.rqRacetrack.setSlot(STATION_ENTRANCES[0], null);
        model.qUnloadBuffer.insertQue(sampleHolder.id);
	}
}
