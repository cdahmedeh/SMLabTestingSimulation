package simModel.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.Racetrack
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 *
 */
public class RacetrackLine {

    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>();
    
    // Queue API
    public boolean hasNext() {
        return sampleHolders.peek() != null;
    }

    public SampleHolder next() {
        return sampleHolders.pop();
    }

    public void queue(final SampleHolder entity) {
        sampleHolders.add(entity);
    }
}
