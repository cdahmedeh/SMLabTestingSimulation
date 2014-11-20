package simModel.action;

import simModel.SMLabModel;
import simModel.entity.RQRacetrack;
import simModel.entity.ICSampleHolder;
import absmodJ.ConditionalAction;

public class EnterUnloadBuffer extends ConditionalAction {
	private SMLabModel model;

	public static boolean precondition(SMLabModel model) {
		ICSampleHolder holder = model.rqRacetrack.peek(RQRacetrack.STATION_ENTRANCES[0]);
        
        return model.rqRacetrack.isTaken(RQRacetrack.STATION_ENTRANCES[0]) && model.qUnloadBuffer.hasVacancy()
                && (
                        holder.hasSample()  && holder.getSample().hasCompletedSequence() || 
                        !holder.hasSample() && model.qUnloadBuffer.emptySampleHolderCount < model.qUnloadBuffer.MAX_EMPTY_HOLDERS
                   );
	}
	
	public EnterUnloadBuffer(SMLabModel model) {
		this.model = model;
	}

	@Override
	public void actionEvent() {
	       //Then move the holder onto the rqRacetrack. 
        ICSampleHolder icSampleHolder = model.rqRacetrack.take(RQRacetrack.STATION_ENTRANCES[0]);
        
        if(!icSampleHolder.hasSample()) {
        	model.qUnloadBuffer.emptySampleHolderCount++;
        }
            
        model.qUnloadBuffer.insertQue(icSampleHolder);
	}
}
