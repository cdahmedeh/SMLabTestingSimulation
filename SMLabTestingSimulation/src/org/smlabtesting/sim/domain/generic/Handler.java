package org.smlabtesting.sim.domain.generic;

/**
 * A state handler is used to process states in an entity. It is recommend to
 * implement these as anonymous classes for brevity.
 * 
 * Not all methods have to be overridden when creating a handler. The ones that
 * are usually overridden are:
 *  condition(), begin(), duration(), end().
 * 
 * @author Ahmed El-Hajjar
 *
 */
public abstract class Handler {
    
    // A reference to the state that this handler processes.
    private final State state;
    public State getState() {return state;}
      
    /**
     * Creates a state handler to process the provided state.
     * 
     * @param state The state that will be processed.
     */
    public Handler(State state) {
        this.state = state;
    }
    
    /**
     * Checks if this state can be be executed. 
     * 
     * By default, the state can always be executed.
     *   
     * @return true if execution can begin, false otherwise.
     */
    public boolean condition() {
        return true;
    };
    
    /**
     * Runs processing steps when entering state.
     */
    public void begin() {
        
    }
    
    /**
     * How long to hold the state for between begin and end points. Used to
     * simulation processing steps that take more than one time point.
     * 
     * By default, the processing time is instantaneous.
     * 
     * @return The number of time points to halt for. Usually in seconds unit.
     */
    public int duration() {
        return 0;
    }
    
    /**
     * Runs processing steps when exiting state.
     */
    public void end() {
        
    }
}
