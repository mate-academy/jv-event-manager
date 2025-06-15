package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ExecutorService executorService;
    private List<EventListener> listeners;

    public EventManager() {
        this.executorService = Executors.newFixedThreadPool(10);
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        listeners.forEach(f -> CompletableFuture.runAsync(() -> f.onEvent(event),
                executorService));
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
