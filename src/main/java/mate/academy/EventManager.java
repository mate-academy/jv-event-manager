package mate.academy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private Map<String, EventListener> listeners;
    private ExecutorService executor;

    public EventManager() {
        this.listeners = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        String listenerKey = String.valueOf(listener.hashCode());
        listeners.put(listenerKey, listener);
    }

    public void deregisterListener(EventListener listener) {
        String listenerKey = String.valueOf(listener.hashCode());
        listeners.remove(listenerKey);
    }

    public void notifyEvent(Event event) {
        for (var listener : listeners.entrySet()) {
            executor.submit(() -> listener.getValue().onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
