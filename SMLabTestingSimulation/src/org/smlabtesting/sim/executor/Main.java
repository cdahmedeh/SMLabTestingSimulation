package org.smlabtesting.sim.executor;

import org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine;
import org.smlabtesting.sim.domain.entity.loadunload.NewSamples;
import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.loadunload.UnloadBuffer;
import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.entity.testing.TestCellBuffer;
import org.smlabtesting.sim.domain.entity.testing.TestingMachine;
import org.smlabtesting.sim.logging.LogPrinter;

/**
 * This shows how to run a simulation.
 *
 * @author Ahmed El-Hajjar
 */
public class Main {
    // The printer used for threaded console logging.
    public static final LogPrinter printer = new LogPrinter();
    
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
//            simulation.addEntity(sampleHolder);
        }

        // Create the load/unload machine.
        final NewSamples newSamples = new NewSamples();
        simulation.addEntity(newSamples);

        final UnloadBuffer unloadBuffer = new UnloadBuffer(racetrack);
        simulation.addEntity(unloadBuffer);

        final RacetrackLine racetrackLine = new RacetrackLine(racetrack, 0);
        simulation.addEntity(racetrackLine);

        final LoadUnloadMachine loadUnloadMachine = new LoadUnloadMachine(newSamples, unloadBuffer, racetrackLine);
        simulation.addEntity(loadUnloadMachine);
        
        // Create the test cells.
        for (int stationId = 1; stationId <= 5; stationId++) {
            final TestCellBuffer testCellBuffer = new TestCellBuffer(racetrack, stationId);
            simulation.addEntity(testCellBuffer);

            final RacetrackLine racetrackLine_ = new RacetrackLine(racetrack, stationId);
            simulation.addEntity(racetrackLine_);

            // Create testing machines in test cells.
            for (int machineId = 0; machineId < 3; machineId++) {
                final TestingMachine testingMachine = new TestingMachine(racetrackLine_, testCellBuffer, stationId, machineId);
                simulation.addEntity(testingMachine);
            }
        }

        // Simulate for an hour seconds.
        while (simulation.getTime() < 3600) {
            simulation.process();
            
            // Printing is slow, but not anymore!
            printer.println(simulation.getGlance());
        }
        
        printer.close();
    }
}
