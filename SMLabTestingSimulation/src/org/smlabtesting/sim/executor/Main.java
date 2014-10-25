package org.smlabtesting.sim.executor;

import org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine;
import org.smlabtesting.sim.domain.entity.loadunload.NewSamples;
import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.loadunload.UnloadBuffer;
import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;

/**
 * This shows how to run a simulation.
 *
 * @author Ahmed El-Hajjar
 */
public class Main {
    public static int total = 0;;

    public static void main(final String[] args) {
        // Create an instance of the simulation run.
        Simulation simulation = new Simulation();

        // Create the racetrack.
        Racetrack racetrack = new Racetrack();
        simulation.addEntity(racetrack);

        // Create some sample holders, put them in the racetrack, add them also to the simulation.
        for (int i = 0; i < 10; i++) {
            final SampleHolder sampleHolder = new SampleHolder();
            racetrack.setSlot(i, sampleHolder);
            simulation.addEntity(sampleHolder);
        }

        // Create the load/unload machine.
        final NewSamples newSamples = new NewSamples();
        simulation.addEntity(newSamples);

        final UnloadBuffer unloadBuffer = new UnloadBuffer(racetrack);
        simulation.addEntity(unloadBuffer);

        final RacetrackLine racetrackLine = new RacetrackLine(racetrack);
        simulation.addEntity(racetrackLine);

        final LoadUnloadMachine loadUnloadMachine = new LoadUnloadMachine(newSamples, unloadBuffer, racetrackLine);
        simulation.addEntity(loadUnloadMachine);

        // Simulate for an hour seconds.
        while (simulation.getTime() < 3600) {
            simulation.process();
            
            // Printing is slow.
            System.out.println(simulation.getGlance());
        }
    }
}
