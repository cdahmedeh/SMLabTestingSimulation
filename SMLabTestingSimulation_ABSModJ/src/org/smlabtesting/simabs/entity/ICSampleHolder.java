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
     * Retrieves a reference to the icSample in this holder without removing it 
     * from the holder.
     * 
     * @return A reference to the icSample in the holder. Null if none.
     */    
    public ICSample getSample(){
        return this.sample;
    }
}
