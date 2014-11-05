package org.smlabtesting.sim.executor;

import static org.smlabtesting.sim.executor.EntityState.Step.*;

import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;

/**
 * This class handles the relationship between an entity instance, and its
 * processing state. Once instance of this class should be created per 
 * Entity.
 * 
 * It also handles the state processing and waiting.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class EntityState {
    // The processing cycle steps that an entity goes through. 
    public enum Step {Idle, Begin, Pause, End;}
    
    // Fields
    private final Entity entity;        // The entity that is being processed.
    private final Handler[] handlers;   // All handlers that belong to this entity.
    
    private Handler handler = null;     // The current handler that is being used to processed this entity.
    private Step step = Idle;           // The current step in the processing cycle for this entity.
    private int left = 0;               // The amount of time steps left until the entity finishes the Pause cycle.

    /**
     * Creates an instance of an entity state representation.
     * 
     * @param entity The entity to handle.
     */
    public EntityState(Entity entity) {
        this.entity = entity;
        handlers = entity.generateHandlers();
    }
    
    /**
     * Processes the referred entity using it's handlers. Should be called once
     * per simulation time advance.
     * 
     * The idea is to first check if any of the state handlers have 
     * preconditions that warrant their start. Once that is the case, that
     * handler is used to process the entity. Only one handler can be executed
     * at a time in a entity.
     */
    public void process() {
        // Nothing needs to be done for entity that have no state processors.
        if (handlers.length == 0) {
            return;
        }
        
        // If no other handler is doing something, then check to see if any of 
        // the handlers have preconditions which are true.
        if (step == Idle) {
            for (Handler hndlr: handlers) {
                if (hndlr.condition()) {
                    handler = hndlr;
                    step = Begin;
                    break;
                }
            } 
        } 
        
        // If so, then start processing the entity.
        if (handler != null) {
            // Run pre-waiting processing, and calculate a waiting time.
            if (step == Begin) {
                handler.begin();
                left = handler.duration();
                step = Pause;
            }
            
            // Wait until there is no time left.
            if (step == Pause) {
                if (left > 0) {
                    left--;
                } else {
                    step = End;
                }
            }
            
            // Once done, run the terminating processing steps, and be ready for
            // using other handlers.
            if (step == End) {
                handler.end();
                handler = null;
                step = Idle;

            }
        }
    }
    
    /**
     * Gives some information and the entity, and this state processor.
     * 
     * @return A string with some information.
     */
    public String getGlance() {
        return String.format(
                "%s -- {%s}",
                entity.getGlance(),
                handler == null ? "" : handler.getState()
        );
    }
    
    @Override
    public int hashCode() {
        // Use the entity's hash for performance and faster lookup for deletion.
        return entity.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        // Use the entity's hash for performance and faster lookup for deletion.
        return hashCode() == obj.hashCode();
    }
}
