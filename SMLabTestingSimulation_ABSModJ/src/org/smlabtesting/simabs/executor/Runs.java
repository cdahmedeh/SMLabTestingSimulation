package org.smlabtesting.simabs.executor;

import java.util.ArrayList;
import java.util.List;

public class Runs {
	List<Run> runs = new ArrayList<>();

	private int count;
	
	public Runs(int count) {
		this.count = count;

	}
	
	public void add(Run run) {
		this.runs.add(run);
	}
	
	public void printAverages(int interval) {
		double[] percentageLateRegularSamples = new double[count]; 
		double[] percentageLateRushSamples = new double[count];
		
		for (int i = 0; i < count; i++) {
			for (Run run : runs) {
				run.outputs.get(i*interval).percentageLateRegularSamples();
				run.outputs.get(i*interval).percentageLateRushSamples();
				
				percentageLateRegularSamples[i] += run.outputs.get(i*interval).percentageLateRegularSamples;
				percentageLateRushSamples[i] += run.outputs.get(i*interval).percentageLateRushSamples;
			}
			
			percentageLateRegularSamples[i] /= count;
			percentageLateRushSamples[i] /= count;
			
			System.out.print(i);
			System.out.print("\t");
			System.out.print(percentageLateRegularSamples[i]);
			System.out.print("\t");
			System.out.print(percentageLateRushSamples[i]);
			System.out.print("\n");
		}
	}
}
