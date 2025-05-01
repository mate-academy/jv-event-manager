package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
        listeners.addIfAbsent(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        for (EventListener listener : listeners) {
            executor.submit(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    System.err.println("Error notifying listener: " + e.getMessage());
                }
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
