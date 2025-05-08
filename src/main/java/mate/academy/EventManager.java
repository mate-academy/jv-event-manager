package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private static final long TERMINATION_TIMEOUT = 30;
    private static final int NUM_OF_THREADS = 5;

    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                System.out.println("Executor did not terminate");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while shutting down");
        }
        listeners.clear();
    }
}
