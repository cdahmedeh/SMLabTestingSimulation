package org.smlabtesting.simabs.variable;

import org.smlabtesting.simabs.model.SMLabModel;

public class Output{
	@SuppressWarnings("unused")
	private SMLabModel model;
	
	//SSOVs
	public int[] totalFailedStationEntries = new int[]{0,0,0,0,0,0};
	public int lateRegularSamples = 0;
	public int lateRushSamples = 0;
	public int totalNumRegularSamples = 0;
	public int totalNumRushSamples = 0;
	
	//DSOVs
	public double percentageLateRegularSamples = 0;
	public double percentageLateRushSamples = 0;
	

	public Output(SMLabModel model) {
		this.model = model;
	}
	
	public double percentageLateRegularSamples()
	{
		if(totalNumRegularSamples > 0)
			percentageLateRegularSamples = (double) lateRegularSamples / (double) totalNumRegularSamples;
		
		return percentageLateRegularSamples;
	}
	
	public double percentageLateRushSamples()
	{
		if(totalNumRushSamples > 0)
			percentageLateRushSamples = (double) lateRushSamples / (double) totalNumRushSamples;
		
		return percentageLateRushSamples;
	}
	
	public void reset() {
		totalFailedStationEntries = new int[]{0,0,0,0,0,0};
		lateRegularSamples = 0;
		lateRushSamples = 0;
		totalNumRegularSamples = 0;
		totalNumRushSamples = 0;
		percentageLateRegularSamples = 0;
		percentageLateRushSamples = 0;
	}
}
