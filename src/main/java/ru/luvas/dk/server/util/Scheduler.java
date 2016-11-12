package ru.luvas.dk.server.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Scheduler {

    @Getter
    private final static ExecutorService executor = Executors.newCachedThreadPool();
    
    public static void run(Runnable runnable) {
        executor.execute(runnable);
    }
    
    public static void run(Runnable runnable, long delay, TimeUnit timeUnit) {
        executor.execute(() -> {
            sleep(timeUnit.toMillis(delay));
            runnable.run();
        });
    }
    
    public static void run(Runnable runnable, long delay, long interval, TimeUnit timeUnit) {
        executor.execute(() -> {
            sleep(timeUnit.toMillis(delay));
            while(true) {
                runnable.run();
                sleep(timeUnit.toMillis(interval));
            }
        });
    }
    
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        }catch(InterruptedException ex) {}
    }
    
}
