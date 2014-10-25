package org.smlabtesting.sim.domain.generic;

import static org.smlabtesting.sim.domain.generic.EntityTemplate.EntityTemplateState.*;
import static org.smlabtesting.sim.executor.Simulation.DEFAULT_RNG;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.smlabtesting.sim.domain.entity.Racetrack;
import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;

/**
 * Maps to _insert CM identifier here_
 * 
 * This class shows how an entity should be implemented.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class EntityTemplate extends Entity implements Queue<Entity> {
    // Constants
    /* Anything that you will need to refer to often and doesn't change
     * Things like queue sizes, default indexes, distribution model bounds, etc.
     */
    public static final int QUEUE_COUNT = 3;
    public static final int ENTRY_POINT_INDEX = 0;
    public static final double PROBABILITY_MEAN = 10;
    public static final double[] PROBABILITY_SD = new double[]{1.0, 3.0, 0.5};
    
    // States
    /* Create a protected enum that has all the possible states of the entity.
     * They usually map to activities in the CM behavioral diagrams.
     * 
     * The enum MUST implement State.
     * The enum should be called {EntityName}State
     * 
     * Use static imports so you don't have a very long state name.
     */
    protected enum EntityTemplateState implements State {
        DoingNothing,
        DoingSomething,
        WastingTime;
    }
    
    // Counters/Delays
    /*
     * Of course some operations take time to perform in the system. For 
     * example running a test takes a few minutes. Put variables that 
     * are used to count time remaining, etc...
     * 
     * Make sure they are initialized.
     */
    private int timeRemaining = 0;

    // RNG
    /* Any distribution models should be listed here. If you are using 
     * Apache Math, make sure you use the same RNG under 
     * Simulation.DEFAULT_RNG
     * 
     * Make sure they are initialized and constructed. We don't want to waste
     * time re-seeding every time.
     */
    private NormalDistribution normalDistribution = new NormalDistribution(
            DEFAULT_RNG, PROBABILITY_MEAN, PROBABILITY_SD[0]
    );    
    
    // Containers
    /* Any storage mechanism that this entity has a listed here. THIS IS NOT 
     * THE SAME AS A RELATIONSHIP, as this is created by the entity, and 
     * does not come from outside the entity. Should be initialized
     * and final.
     */
    private final Deque<Entity> entityQueue = new ArrayDeque<>(QUEUE_COUNT);
   
    // Relationships
    /* Anything that this entity links to, but does not create itself. For 
     * example, the load/unload machine is linked to it's buffer spaces queue.
     * 
     * These would be usually set in the constructor.
     */
    private Racetrack racetrack;
    private RacetrackLine racetrackLine;
    
    // Factories
    /* If you often create this Entity, and want to have a consistent way
     * of generating, you can use a factory. Make sure the method is static.
     */
    public static Entity generateEntity() {
        return new EntityTemplate(/*some parameters you really want to have*/);
    }
    
    // Constructs
    /* Constructors go here. 
     */
    public EntityTemplate() { 
    }
    
    public EntityTemplate(Racetrack racetrack, RacetrackLine racetrackLine) {
        this.racetrack = racetrack;
        this.racetrackLine = racetrackLine;
    }
    
    // Entity API
    /* This is where the entity API methods should be implemented.
     */
    @Override
    public void process() {
        /*
         * This is the most important part of the whole simulation, you need
         * to be really careful here and keep everything neat and 
         * consistent, otherwise, things will break in funny and unexpected ways.
         */

        /*
         * Every single entity should have this block. This sets the initial
         * state at the first processing step. 
         * 
         */
        // Initial state
        if (noState()) {
            setState(DoingNothing);
        }
        
        /*
         * This is an example of a typical activity
         */
        
        /*
         * You want to check that you are in some state before doing anything.
         * Unless of course, the entity is always in the same one. 
         */
        if (isState(DoingNothing)) {
            /*
             * You can include some preconditions for doing some processing.
             */
            if (true /* some conditions*/) {
                // do some stuff over here
                // remove stuff from queues, moving things, over, etc...
                
                /*
                 * if you switch states abruptly make sure you do a  
                 * return, especially if you intend preemption
                 */
                setState(DoingSomething); 
                return; /* be very careful */
            }
        }
        
        /*
         * Again, notice the pattern, you check what state you are in,
         * and the do something about it.
         * 
         * This pattern makes it very easy to translation straight from
         * the behavior diagram.
         */
        if (isState(DoingSomething)) {
            /* Here is an example of how to wait if something will take more
             * than one step of time. 
             */
            
            // remember that you need to have set time remaining to a non-zero valuesome where else
            if (timeRemaining > 0) {
                timeRemaining--; 
                return; //make sure you do this
            }
            
            /*
             * Done waiting so now you can do more processing.
             */
        }
        
        /*
         * one more time, you get the idea by now.
         */
        if (isState(WastingTime)) {
            // do stuff over here...
        }
    }

    @Override
    public String getGlance() {
        /*
         * Make a one liner about the entity. Just use String.format 
         */
        return String.format("[EntityTemplate] has %d items", entityQueue.size());
    }

    // Queue API
    /* This is where the queue API methods should be implemented if your entity
     * implements it. See Queue for some methods comments on what each one does.
     */
    @Override
    public boolean hasNext() {
        return entityQueue.peek() != null;
    }

    @Override
    public Entity next() {
        return entityQueue.pop();
    }

    @Override
    public boolean hasVacancy() {
        return entityQueue.size() < QUEUE_COUNT;
        // for unlimited queues, just always return true.
    }
    
    @Override
    public void queue(final Entity entity) {
        entityQueue.add(entity);
    }
    
    // Public methods
    /**
     * Any methods you want to expose to the outside go here.
     * 
     */
    public boolean messAroundWithThisEntity(String how) {
        return true;
    }
    
    
    // Private methods
    /**
     * Any repeatable piece of code you want to keep to yourself... 
     * 
     */
}
