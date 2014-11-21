package org.smlabtesting.simabs.variable;

import org.smlabtesting.simabs.model.SMLabModel;

public class Output{
	@SuppressWarnings("unused")
	private SMLabModel model;
	
	//SSOVs
	public int[] totalFailedStationEntries = new int[]{0,0,0,0,0,0};

	public Output(SMLabModel model) {
		this.model = model;
	}
}
