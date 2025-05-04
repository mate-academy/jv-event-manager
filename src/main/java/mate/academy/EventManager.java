package mate.academy;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private final Set<EventListener> listeners = ConcurrentHashMap.newKeySet();
    private final ExecutorService executor;

    public EventManager() {
        this.executor = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executor.submit(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception exception) {
                    System.err.println("Error in listener " + listener + ": "
                            + exception.getMessage());
                }
            });

        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdown();
            }
        } catch (InterruptedException exception) {
            executor.shutdown();
            Thread.currentThread().interrupt();
        }
    }
}
