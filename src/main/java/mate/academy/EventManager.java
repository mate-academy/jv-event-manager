package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private static final int THREAD_POOL_SIZE = 4;
    private static final int TERMINATION_TIMEOUT = 10;

    private List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            CompletableFuture.runAsync(() -> listener.onEvent(event), executor);
        }
    }

    public void shutdown() {
        try {
            if (!executor.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.SECONDS)) {
                executor.shutdown();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread " + Thread.currentThread().getName()
                    + " was interrupted", e);
        }
    }
}
