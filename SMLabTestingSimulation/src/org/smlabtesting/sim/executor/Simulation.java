package org.smlabtesting.sim.executor;

import java.util.ArrayList;
import java.util.List;

import org.smlabtesting.sim.domain.generic.Entity;

/**
 * An instance of a simulation run.
 * 
 * @author Ahmed El-Hajjar
 */
public class Simulation {
	// All the entities to be simulated. Should be sorted according to their relationships.
	private List<Entity> entities = new ArrayList<>();
	
	// The current discrete time point. 
	private long time = 0L;
	public long getTime() {return time;}

	/**
	 * Steps the simulation one time unit forward.
	 */
	public void process() {
		for (Entity entity: entities) {
			entity.process();
		}
		
		time++;
	}

	/**
	 * Adds an entity to the simulation.
	 * 
	 * @param entity The entity to add.
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Entity entity: entities) {
			sb.append(entity.toString());
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
