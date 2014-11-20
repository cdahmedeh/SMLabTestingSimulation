package org.smlabtesting.simabs.entity;

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
public class QNewSamples {
    // Containers
    public final Deque<ICSample> icSamples = new ArrayDeque<ICSample>();

    // Queue API
    public boolean hasNext() {
        return icSamples.peek() != null;
    }

    public ICSample removeQue() {
        return icSamples.pop();
    }

    public void insertQue(final ICSample entity) {
        icSamples.add(entity);
    }
}
