package org.smlabtesting.sim.logging;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Console logger that does printing on a separate thread to prevent the 
 * application from slowing due to logging. Must be destroyed otherwise, the
 * program will hang.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class LogPrinter {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    /**
     * Prints the provided string to the console. May not happen instantly as
     * actual printing is done in a thread pool. This method is non-blocking.
     * 
     * A new line is appended after the provided string.
     * 
     * @param string The string to print to the console.
     */
    public void println(final String string) {
        
      //  Runnable t = n
        executor.execute(  new Runnable()
        {
                    public void run(){
                        System.out.println(string);
                        }
        });
       // executor.execute(() -> System.out.println(string));
    }
    
    /**
     * Properly shuts the thread executor down and then waits until all 
     * remaining tasks are executed.
     */
    public void close() {
        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            
        }
    }
}
