package org.smlabtesting.sim.executor;

import org.smlabtesting.sim.domain.entity.Racetrack;
import org.smlabtesting.sim.domain.entity.SampleHolder;

/**
 * This shows how to run a simulation.
 * 
 * @author Ahmed El-Hajjar
 */
public class Main {
	public static void main(String[] args) {
		// Create an instance of the simulation run.
		Simulation simulation = new Simulation();
		
		// Create the racetrack.
		Racetrack racetrack = new Racetrack();
		simulation.addEntity(racetrack);
		
		// Create sample holders, put them in the racetrack, add them also to the simulation.
		for (int i = 0; i < 48; i++) {
			SampleHolder sampleHolder = new SampleHolder();
			racetrack.setSlot(i, sampleHolder);
			simulation.addEntity(sampleHolder);
		}
		
		// Simulate for 10000 time units.
		while (simulation.getTime() < 10000) {
			simulation.process();
		}
		
		// Print simulation state.
		System.out.println(simulation.toString());
	}
}
