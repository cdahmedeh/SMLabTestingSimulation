package org.smlabtesting.simabs.variable;

import java.util.Arrays;

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
	
	public void percentageLateRegularSamples()
	{
		if(totalNumRegularSamples > 0)
			percentageLateRegularSamples = (double) lateRegularSamples / (double) totalNumRegularSamples;
	}
	
	public void percentageLateRushSamples()
	{
		if(totalNumRushSamples > 0)
			percentageLateRushSamples = (double) lateRushSamples / (double) totalNumRushSamples;
	}
	
	public Output copy() {
		Output output = new Output(model);
		output.totalFailedStationEntries = Arrays.copyOf(totalFailedStationEntries, totalFailedStationEntries.length);
		output.lateRegularSamples = this.lateRegularSamples;
		output.lateRushSamples = this.lateRushSamples;
		output.totalNumRegularSamples = this.totalNumRegularSamples;
		output.totalNumRushSamples = this.totalNumRushSamples;
		output.percentageLateRegularSamples = this.percentageLateRegularSamples;
		output.percentageLateRushSamples = this.percentageLateRushSamples;
		return output;
	}
}
