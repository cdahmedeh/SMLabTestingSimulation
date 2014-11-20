package simModel.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import javax.swing.text.html.parser.Entity;

/**
 * Maps to Q.NewSamples.
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class NewSamples {
    // Containers
    public final Deque<Sample> samples = new ArrayDeque<Sample>();

    // Queue API
    public boolean hasNext() {
        return samples.peek() != null;
    }

    public Sample removeQue() {
        return samples.pop();
    }

    public void insertQue(final Sample entity) {
        samples.add(entity);
    }
}
