package org.smlabtesting.simabs.variable;

import org.smlabtesting.simabs.model.SMLabModel;

public class Output{
	@SuppressWarnings("unused")
	private SMLabModel model;
	
	//SSOVs
	public int[] totalFailedStationEntries = new int[]{0,0,0,0,0,0};
	//TODO lateRegularSamples
	//TODO lateRushSamples
	//TODO totalNumRegularSamples
	//TODO totalNumRushSamples
	
	//DSOVs
	//TODO percentageLateRegularSamples
	//TODO percentageLateRushSamples
	

	public Output(SMLabModel model) {
		this.model = model;
	}
}
