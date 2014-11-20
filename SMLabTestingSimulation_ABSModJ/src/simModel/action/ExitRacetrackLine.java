package simModel.action;

import simModel.ModelName;
import simModel.entity.RQRacetrack;
import absmodJ.ConditionalAction;

public class ExitRacetrackLine extends ConditionalAction {

	private ModelName model;
	private int stationId;

	public ExitRacetrackLine(ModelName model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(ModelName model, int stationId) {
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
