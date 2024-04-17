package mate.academy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> listeners;
    private final ExecutorService executor;

    public EventManager() {
        this.listeners = new CopyOnWriteArrayList<>();
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
            executor.execute(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
