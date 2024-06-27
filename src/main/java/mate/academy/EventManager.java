package mate.academy;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventManager {
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    public void registerListener(EventListener listener) {
        if (isShutdown.get()) {
            throw new IllegalStateException("Cannot register listener after shutdown");
        }
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            CompletableFuture.runAsync(() -> listener.onEvent(event), executorService);
        }
    }

    public void shutdown() {
        isShutdown.set(true);
        listeners.clear();
        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
