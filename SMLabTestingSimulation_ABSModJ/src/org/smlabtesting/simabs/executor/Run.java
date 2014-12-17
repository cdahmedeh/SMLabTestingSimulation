package org.smlabtesting.simabs.executor;

import java.util.TreeMap;

import org.smlabtesting.simabs.variable.Output;

public class Run {
	TreeMap<Integer, Output> outputs = new TreeMap<>();
	
	public void add(int interval, Output output) {
		outputs.put(interval, output.copy());
	}
}
