package simModel.action;

import simModel.ModelName;
import simModel.entity.Racetrack;
import simModel.entity.SampleHolder;
import absmodJ.ConditionalAction;

public class EnterTestCellBuffer extends ConditionalAction {
	
	private ModelName model;
	private int stationId;

	public EnterTestCellBuffer(ModelName model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(ModelName model, int stationId) {
		SampleHolder holder = model.racetrack.peek(Racetrack.STATION_ENTRANCES[stationId]);
        return model.testCellBuffer[stationId].hasVacancy()
                && holder != null
                && holder.hasSample()
                && holder.getSample().hasNextTest(stationId);
	}
	

	@Override
	public void actionEvent() {
        //Then move the holder onto the buffer. 
        SampleHolder sampleHolder = model.racetrack.take(Racetrack.STATION_ENTRANCES[stationId]);
        model.testCellBuffer[stationId].insertQue(sampleHolder);
		
	}
}

