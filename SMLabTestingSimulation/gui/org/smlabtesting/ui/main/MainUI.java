package org.smlabtesting.ui.main;

import javax.swing.JFrame;

import org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine;
import org.smlabtesting.sim.domain.entity.loadunload.NewSamples;
import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.loadunload.UnloadBuffer;
import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.executor.Simulation;

public class MainUI {
    public static void main(final String[] args) {
        // Create an instance of the simulation run.
        Simulation simulation = new Simulation();

        // Create the racetrack.
        Racetrack racetrack = new Racetrack();
        simulation.addEntity(racetrack);

        // Create some sample holders, put them in the racetrack, add them also to the simulation.
        for (int i = 0; i < 48; i++) {
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
        
        // ------
                
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("SM Lab Testing");
        
        frame.setBounds(100, 100, 800, 600);
        
        frame.setVisible(true);
        
        // -----
        
        StructurePanel panel = new StructurePanel(racetrack, loadUnloadMachine, unloadBuffer, racetrackLine);
        frame.getContentPane().add(panel);
        
        // -----
        
        long start = System.currentTimeMillis();
        
//        while (simulation.getTime() < 365*24*3600) {
        while (simulation.getTime() < 60*3600) {
//        while (true) {
            simulation.process();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            frame.repaint();
        }
        
        long end = System.currentTimeMillis();
        
        System.out.println(end - start);
    }
    
    
}
