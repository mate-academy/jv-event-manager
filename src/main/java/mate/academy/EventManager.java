package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    public synchronized void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public synchronized void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public synchronized void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executor.submit(() -> {
                synchronized (EventManager.this) {
                    if (!listeners.contains(listener)) {
                        return;
                    }
                }
                listener.onEvent(event);
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
