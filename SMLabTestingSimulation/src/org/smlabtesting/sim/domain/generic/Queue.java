package org.smlabtesting.sim.domain.generic;

/**
 * Queue entities should implement this.
 * 
 * @author Ahmed El-Hajjar
 *
 * @param <E> The type of Entity that will be queued.
 */
public interface Queue<E extends Entity> {
    /**
     * Checks to see if an entity is waiting in the queue to be processed.
     * 
     * @return True if there is one.
     */
    public boolean hasNext();
    
    /**
     * Removes the Entity that is next in line, and returns a reference to it.
     * 
     * @return The entity that was just removed from the line, that was next in line.
     */
    public E next();
    
    /**
     * Checks to see if there is room left in the queue.
     * 
     * @return If there is room in the queue.
     */
    public boolean hasVacancy();
    
    /**
     * Puts the provided entity in line in the queue.
     * 
     * @param entity The entity to queue.
     */
    public void queue(E entity);
}
