package org.smlabtesting.sim.domain.generic;

import org.smlabtesting.sim.executor.Simulation;

/**
 * Represents a simulation entity object. The class defines the structure of
 * an entity, and the object instances represent an entity instance.
 *
 * All entities are sub-classes of this type.
 *
 * @author Ahmed El-Hajjar
 */
public abstract class Entity {

    // Describes the current state of the entity.
    // Usually equivalent to a node in the behavioral graph in the CM.
    private State state = null;
    public State getState() {return state;}
    public void setState(final State state) {this.state = state;}
    public boolean noState() {return state == null;}
    public boolean isState(final State state) {return this.state == state;}
    
    // Counts a duration to signify a state taking more than one processing step.
    private int time = 0;
    public void pause(int time) {this.time = time;}
    public boolean paused() {return this.time == 0;}
    public void count() {this.time--;}    
    
    // A reference to the current simulation. Set by Simulation.
    protected Simulation simulation = null;
    public Simulation getSimulation() {return simulation;}
    public void setSimulation(Simulation simulation) {this.simulation = simulation;}

    /**
     * Processes the entity to the next discrete time step in the simulation.
     * The result of the change depends on the current state of the entity and
     * the state of any of its relationships.
     *
     * This should be based on the behavioral graphs of the CM.
     */
    public abstract void process();
    
    /**
     * @return Should return a one liner about some basic information about this 
     *          entity.  
     */
    public abstract String getGlance();
}
