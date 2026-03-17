package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ConcurrentLinkedQueue<EventListener> listeners
            = new ConcurrentLinkedQueue<>();
    private final ExecutorService executor
            = Executors.newFixedThreadPool(10);

    public void registerListener(EventListener listener) {
        listeners.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            if (!executor.isShutdown()) {
                executor.submit(() -> listener.onEvent(event));
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
        listeners.clear();
    }
}
