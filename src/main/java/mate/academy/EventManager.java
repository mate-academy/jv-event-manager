package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ConcurrentLinkedQueue<EventListener> queue =
            new ConcurrentLinkedQueue<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        queue.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        queue.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : queue) {
            executor.submit(() -> {
                listener.onEvent(event);
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
        queue.clear();
    }
}
