package simModel.action;

import simModel.ModelName;
import simModel.entity.Racetrack;
import simModel.entity.SampleHolder;
import absmodJ.ConditionalAction;

public class EnterUnloadBuffer extends ConditionalAction {
	private ModelName model;

	public static boolean precondition(ModelName model) {
		SampleHolder holder = model.racetrack.peek(Racetrack.STATION_ENTRANCES[0]);
        
        return model.racetrack.isTaken(Racetrack.STATION_ENTRANCES[0]) && model.unloadBuffer.hasVacancy()
                && (
                        holder.hasSample()  && holder.getSample().hasCompletedSequence() || 
                        !holder.hasSample() && model.unloadBuffer.emptySampleHolderCount < model.unloadBuffer.MAX_EMPTY_HOLDERS
                   );
	}
	
	public EnterUnloadBuffer(ModelName model) {
		this.model = model;
	}

	@Override
	public void actionEvent() {
	       //Then move the holder onto the racetrack. 
        SampleHolder sampleHolder = model.racetrack.take(Racetrack.STATION_ENTRANCES[0]);
        
        if(!sampleHolder.hasSample()) {
        	model.unloadBuffer.emptySampleHolderCount++;
        }
            
        model.unloadBuffer.queue(sampleHolder);
	}
}
