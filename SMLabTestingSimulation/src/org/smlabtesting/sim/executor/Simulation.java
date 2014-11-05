package org.smlabtesting.sim.executor;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;
import org.smlabtesting.sim.domain.generic.Entity;

/**
 * An instance of a simulation run. This simulator uses the "Activity Scanning
 * World View" as its execution model.
 *
 * @author Ahmed El-Hajjar
 */
public class Simulation {
    // The random number generator used by the application.
    public static final RandomGenerator DEFAULT_RNG = new Well19937a();

    // All the entities to be simulated. Should be sorted according to their relationships topologically.
    // The entities are paired to their state using EntityState.
    private final Set<EntityState> entities = new LinkedHashSet<>();
    
    // Entities waiting to added/removed. To prevent iterators from failing.
    private final Set<EntityState> entitiesToAdd = new LinkedHashSet<>();
    private final Set<EntityState> entitiesToRemove = new LinkedHashSet<>();

    // The current discrete time point.
    private int time = 0;
    public int getTime() {return time;}

    /**
     * Steps the simulation one time unit forward.
     */
    public void process() {
        // Add or remove pending entities.
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
        
        // Process the entities.
        for (final EntityState entity: entities) {
            entity.process();
        }

        // Count time.
        time++;
    }

    /**
     * Adds an entity to the simulation. Added in the next processing step.
     *
     * @param entity The entity to add.
     */
    public void addEntity(final Entity entity) {
        entity.setSimulation(this);
        entitiesToAdd.add(new EntityState(entity));
    }
   
    /**
     * Removes an entity to the simulation. Removed in the next processing step.
     *
     * @param entity The entity to remove.
     */
    public void removeEntity(final Entity entity) {
        // This works because EntityState uses the same hash code as Entity.
        entitiesToRemove.add(new EntityState(entity));
    }

    /**
     * A string with some basic information on all the entities in the 
     * system.
     * 
     * @return A string with some basic information.
     */
    public String getGlance() {
        final StringBuilder sb = new StringBuilder();

        for (final EntityState entity: entities) {
            sb.append(entity.getGlance());
            sb.append('\n');
        }

        return sb.toString();
    }
}
