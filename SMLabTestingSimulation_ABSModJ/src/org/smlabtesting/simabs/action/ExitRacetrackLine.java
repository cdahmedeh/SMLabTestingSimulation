package org.smlabtesting.simabs.action;

import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalAction;

public class ExitRacetrackLine extends ConditionalAction {

	private SMLabModel model;
	private int stationId;

	public ExitRacetrackLine(SMLabModel model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId) {
      // If there is a holder waiting to enter and it's possible to merge
      // on the rqRacetrack.
      return model.qRacetrackLine[stationId].hasNext() && model.rqRacetrack.isVacant(RQRacetrack.STATION_EXITS[stationId]);
	}
	
	@Override
	public void actionEvent() {
	    //Then move the holder onto the rqRacetrack. 
      model.rqRacetrack.setSlot(RQRacetrack.STATION_EXITS[stationId], model.qRacetrackLine[stationId].removeQue());
	}
}
