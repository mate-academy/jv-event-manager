package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private volatile boolean isShutdown = false;

    public void registerListener(EventListener listener) {
        if (!isShutdown) {
            listeners.add(listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (!isShutdown) {
            for (EventListener listener : listeners) {
                executorService.submit(() -> {
                    try {
                        listener.onEvent(event);
                    } catch (Exception e) {
                        System.err.println("Error notifying listener: " + listener
                                + " - " + e.getMessage());
                    }
                });
            }
        }
    }

    public void shutdown() {
        isShutdown = true;
        executorService.shutdown();
    }
}
