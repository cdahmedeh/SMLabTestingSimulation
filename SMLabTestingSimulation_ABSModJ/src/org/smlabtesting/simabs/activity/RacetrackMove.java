package org.smlabtesting.simabs.activity;

import static org.smlabtesting.simabs.variable.Constants.STATION_ENTRANCES;
import static org.smlabtesting.simabs.variable.Constants.TEST_CELL_BUFFER_CAPACITY;
import static org.smlabtesting.simabs.variable.Constants.UNLOADBUFFER_CAPACITY;

import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ScheduledActivity;

/**
 * This action describes the movement of the track as the holders get shifted 
 * forward in a counter-clockwise fashion. Everything is shifted by one slot,
 * every second.
 * 
 * Instead of having a duration 1 second event, and causing confusion for the 
 * occurrence times of this activity, the duration is made instant. Otherwise, 
 * depend on the timing algorithm of the simulation, the racetrack may be moved
 * every two seconds if an event is not immediately re-scheduled, conditions 
 * re-evaluated, and then re-run again.
 * 
 * Also, this condition is never ever false, and this can cause some 
 * interesting issues in the ABCmod simulation algorithms. Thus, when doing a 
 * time advance, this condition will get re-tested again, and again, and never
 * ever become false, thus generating an infinite loop.
 * 
 * In addition, using a time sequence instead of a conditional event with
 * duration means that the other activities don�t have to check that the
 * racetrack is moving.
 *
 * Finally, this activity is responsible for counting missed entrances to 
 * any of the buffers for the stations.
 * 
 * Participants: RQ.Racetrack
 */
public class RacetrackMove extends ScheduledActivity {

	private SMLabModel model;

	public RacetrackMove(SMLabModel model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		return model.getClock() + 1;
	}

	@Override
	public void startingEvent() {
        // Move the belt one slot forward.
        model.udp.shiftRacetrack(model.rqRacetrack);

        // Handle the missed counts for the load/unload machine.
        
		// Used to point to the holder that is at the unload buffer 
		// entrance point. Does not exist in CM.
		RSampleHolder sampleHolder = model.rqRacetrack.slots[STATION_ENTRANCES[0]];
        
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
           	RSampleHolder sampleHolder_ = model.rqRacetrack.slots[STATION_ENTRANCES[stationId]];
         		
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

	@Override
	protected double duration() {
		return 0;
	}
	
	@Override
	protected void terminatingEvent() {
		
	}

}
