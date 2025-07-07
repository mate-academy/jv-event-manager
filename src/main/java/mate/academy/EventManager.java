package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {

    private final ConcurrentLinkedQueue<EventListener> listeners;
    private final ExecutorService executorService;

    public EventManager() {
        this.listeners = new ConcurrentLinkedQueue<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        } else {
            throw new IllegalArgumentException("Listener already registered");
        }
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executorService.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
