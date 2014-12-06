package org.smlabtesting.simabs.action;

import static org.smlabtesting.simabs.variable.Constants.STATION_EXITS;

import org.smlabtesting.simabs.entity.RSampleHolder;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalAction;

/**
 * This action describes when a Sample Holder exits the RacetrackLine queue to
 * merge onto the Racetrack.
 * 
 * Participants: Q.RacetrackLine[stationId]
 * Uses: RQ.Racetrack, R.SampleHolder (implicit), iC.Sample (implicit)
 * 
 * There are six instances of Q.RacetrackLine. There is a separate action for each one.
 * stationId = one of {LU = 0, C1 = 1, C2 = 2, C3, = 3, C4 = 4, C5 = 5}
 */
public class ExitRacetrackLine extends ConditionalAction {
	private SMLabModel model;
	private int stationId;

	public ExitRacetrackLine(SMLabModel model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId) {
      // First check that here is a holder inline to merge onto the track and 
	  // then check if the exit point of this queue onto the racetrack is actually vacant..
		boolean canExit = model.udp.canExitRacetrackQueue(stationId);
		
		return canExit;
	}
	
	@Override
	public void actionEvent() {
	    //Move the sample from the racetrack line queue to the racetrack
		RSampleHolder sampleHolder = model.qRacetrackLine[stationId].removeQue();
		model.rqRacetrack.setSlot(STATION_EXITS[stationId], sampleHolder);
	}
}
