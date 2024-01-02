package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> listeners;
    private final ExecutorService executorService;

    public EventManager() {
        this.listeners = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newFixedThreadPool(6);
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        listeners.forEach(e -> CompletableFuture.runAsync(() -> e.onEvent(event),
                executorService));
    }

    public void shutdown() {
        listeners.clear();
        executorService.shutdown();
    }
}
