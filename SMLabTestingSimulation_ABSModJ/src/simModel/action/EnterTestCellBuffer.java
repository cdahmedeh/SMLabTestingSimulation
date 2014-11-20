package simModel.action;

import simModel.ModelName;
import simModel.entity.RQRacetrack;
import simModel.entity.ICSampleHolder;
import absmodJ.ConditionalAction;

public class EnterTestCellBuffer extends ConditionalAction {
	
	private ModelName model;
	private int stationId;

	public EnterTestCellBuffer(ModelName model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(ModelName model, int stationId) {
		ICSampleHolder holder = model.rqRacetrack.peek(RQRacetrack.STATION_ENTRANCES[stationId]);
        return model.qTestCellBuffer[stationId].hasVacancy()
                && holder != null
                && holder.hasSample()
                && holder.getSample().hasNextTest(stationId);
	}
	

	@Override
	public void actionEvent() {
        //Then move the holder onto the buffer. 
        ICSampleHolder icSampleHolder = model.rqRacetrack.take(RQRacetrack.STATION_ENTRANCES[stationId]);
        model.qTestCellBuffer[stationId].insertQue(icSampleHolder);
		
	}
}

