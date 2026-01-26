package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ConcurrentLinkedQueue<EventListener> eventListenersQueue =
            new ConcurrentLinkedQueue<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public void registerListener(EventListener listener) {
        eventListenersQueue.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListenersQueue.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : eventListenersQueue) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
        eventListenersQueue.clear();
    }
}
