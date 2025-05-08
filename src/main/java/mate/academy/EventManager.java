package mate.academy;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        for (EventListener listener : listeners) {
            CompletableFuture.runAsync(() -> listener.onEvent(event), executorService);
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        listeners.clear();
    }
}
