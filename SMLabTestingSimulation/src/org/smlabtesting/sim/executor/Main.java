package org.smlabtesting.sim.executor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine;
import org.smlabtesting.sim.domain.entity.loadunload.NewSamples;
import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.loadunload.UnloadBuffer;
import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.entity.sampleholder.Sample;
import org.smlabtesting.sim.domain.entity.testingassembly.TestCell;
import org.smlabtesting.sim.domain.entity.testingassembly.TestingMachine;
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

        
        final NewSamples newSamples = new NewSamples();
        simulation.addEntity(newSamples);
        
        final UnloadBuffer unloadBuffer = new UnloadBuffer(racetrack);
        simulation.addEntity(unloadBuffer);

        
        // Create some sample holders, put them in the racetrack, add them also to the simulation.
        for (int i = 0; i < 2; i++) {
            final SampleHolder sampleHolder = new SampleHolder();
            //racetrack.setSlot(i, sampleHolder);
            unloadBuffer.queue(sampleHolder);
            final Sample sample = Sample.generateSample();
            List <Integer> listofTest = new ArrayList();
            listofTest.add(1);
            sample.setTestsRemaining(listofTest);
            
            newSamples.queue(sample);
            //newSamples(sampleHolder);
            simulation.addEntity(sampleHolder);
            simulation.addEntity(sample);
        }

        // Create the load/unload machine.
       
      
        final RacetrackLine racetrackLine = new RacetrackLine(racetrack);
        simulation.addEntity(racetrackLine);

        final Queue <Sample> testedSamples = new LinkedList<Sample>();
        final LoadUnloadMachine loadUnloadMachine = new LoadUnloadMachine(newSamples, unloadBuffer, racetrackLine, testedSamples);
        simulation.addEntity(loadUnloadMachine);
        
        final TestingMachine machine1 = new TestingMachine();
        //final TestingMachine machine2 = new TestingMachine();

        final TestCell testCell = new TestCell(racetrack,1);
        testCell.addTestMachineToCell(machine1);
        //testCell.addTestMachineToCell(machine2);
       simulation.addEntity(machine1);
       simulation.addEntity(testCell);
        
        // Simulate for an hour seconds.
        while (simulation.getTime() < 3600) {
            simulation.process();
            
            // Printing is slow, but not anymore!
            printer.println(simulation.getGlance());
        }
        
        printer.close();
    }
}
