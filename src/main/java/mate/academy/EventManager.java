package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        eventListenersQueue.clear();
    }
}
