package org.smlabtesting.sim.domain.entity.sampleholder;

import org.smlabtesting.sim.domain.generic.Entity;

/**
 * Maps to R.SampleHolder
 *
 * @author Ahmed El-Hajjar
 */
public class SampleHolder extends Entity {
    // Containers
    public Sample sample;

    // Entity API

    @Override
    public String getGlance() {
        return String.format(
                "[SampleHolder] Has sample?: %b", 
                hasSample()
        );
    }

    // Public Methods
    
    /**
     * Inserts the provided sample into this holder. Check for vacancy first
     * with hasSample().
     *  
     * @param sample The sample to insert into the holder.
     */
    public void putSample(final Sample sample) {
        this.sample = sample;
    }

    /**
     * Checks if there is a sample in this holder.
     * 
     * @return True is there a sample in it already.
     */
    public boolean hasSample() {
        return sample != null;
    }
    
    /**
     * Removes the currently installed sample from this holder. It just 
     * disappears from the system.
     * 
     * The calling class is responsible from removing it from the simulation
     * processing.
     * 
     * @return The removed sample. Null if no sample was in.
     */
    public Sample removeSample() {
        Sample removedSample = sample;
        sample = null;
        return removedSample;
    }
    
    /**
     * Retrieves a reference to the sample in this holder without removing it 
     * from the holder.
     * 
     * @return A reference to the sample in the holder. Null if none.
     */    
    public Sample getSample(){
        return this.sample;
    }
}
