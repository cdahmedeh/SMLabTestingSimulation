package org.smlabtesting.sim.domain.generic;

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
	protected State state = null;
	public State getState() {return state;}
	public void setState(State state) {this.state = state;}

	/**
	 * Processes the entity to the next discrete time step in the simulation. 
	 * The result of the change depends on the current state of the entity and 
	 * the state of any of its relationships. 
	 * 
	 * This should be based on the behavioral graphs of the CM.
	 */
	public abstract void process();
}
