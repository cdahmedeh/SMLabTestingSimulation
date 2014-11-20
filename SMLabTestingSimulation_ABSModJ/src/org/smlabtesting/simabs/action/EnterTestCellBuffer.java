package org.smlabtesting.simabs.action;

import org.smlabtesting.simabs.entity.ICSampleHolder;
import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ConditionalAction;

public class EnterTestCellBuffer extends ConditionalAction {
	
	private SMLabModel model;
	private int stationId;

	public EnterTestCellBuffer(SMLabModel model, int stationId) {
		this.model = model;
		this.stationId = stationId;
	}
	
	public static boolean precondition(SMLabModel model, int stationId) {
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

