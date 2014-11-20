package org.smlabtesting.simabs.entity;


/**
 * Maps to R.SampleHolder
 *
 * @author Ahmed El-Hajjar
 * @author Lalit Azad
 */
public class ICSampleHolder {
    // Containers
    public ICSample sample;

    // Public Methods
    
    /**
     * Inserts the provided icSample into this holder. Check for vacancy first
     * with hasSample().
     *  
     * @param icSample The icSample to insert into the holder.
     */
    public void putSample(final ICSample icSample) {
        this.sample = icSample;
    }

    /**
     * Checks if there is a icSample in this holder.
     * 
     * @return True is there a icSample in it already.
     */
    public boolean hasSample() {
        return sample != null;
    }
    
    /**
     * Removes the currently installed icSample from this holder. It just 
     * disappears from the system.
     * 
     * The calling class is responsible from removing it from the simulation
     * processing.
     * 
     * @return The removed icSample. Null if no icSample was in.
     */
    public ICSample removeSample() {
        ICSample removedSample = sample;
        sample = null;
        return removedSample;
    }
    
    /**
     * Retrieves a reference to the icSample in this holder without removing it 
     * from the holder.
     * 
     * @return A reference to the icSample in the holder. Null if none.
     */    
    public ICSample getSample(){
        return this.sample;
    }
}
