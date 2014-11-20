package org.smlabtesting.simabs.variable;

import java.util.HashMap;
import java.util.Map;

import cern.jet.random.engine.RandomSeedGenerator;

/**
 * Modified to provide seeds on demand instead of having to determine number
 * in advance. 
 * 
 * Just uses a map, and every time a seed 'i' is requested, but not created yet
 * it will create it. Otherwise, it just provides the previously provided one.
 * 
 * For deterministic runs, one can use setSeed(..).
 * 
 * Can also be in iterator fashion with next().
 */
public class Seeds {
	private final RandomSeedGenerator rsg;
	private Map<Integer, Integer> seeds = new HashMap<>();
	private int current = 0;

	public Seeds(RandomSeedGenerator rsg) {
		this.rsg = rsg;
	}
	
	public int getSeed(int i) {
		if (seeds.get(i) == null) {
			seeds.put(i, rsg.nextSeed());
		}
		return seeds.get(i);
	}
	
	public void setSeed(int i, int seed) {
		seeds.put(i, seed);
	}
	
	public int next() {
		return getSeed(current++);
	}
}
