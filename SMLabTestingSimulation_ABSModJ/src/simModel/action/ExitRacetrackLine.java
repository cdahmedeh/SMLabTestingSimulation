package simModel.action;

import simModel.ModelName;
import simModel.entity.Racetrack;
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
      // on the racetrack.
      return model.racetrackLine[stationId].hasNext() && model.racetrack.isVacant(Racetrack.STATION_EXITS[stationId]);
	}
	
	@Override
	protected void actionEvent() {
	    //Then move the holder onto the racetrack. 
      model.racetrack.setSlot(Racetrack.STATION_EXITS[stationId], model.racetrackLine[stationId].next());
	}
}
