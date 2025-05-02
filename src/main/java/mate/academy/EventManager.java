package mate.academy;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventManager {
    private final Queue<EventListener> listenersQueue = new ConcurrentLinkedQueue<>();

    public void registerListener(EventListener listener) {
        listenersQueue.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenersQueue.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener l : listenersQueue) {
            CompletableFuture.runAsync(() -> l.onEvent(event));
        }
    }

    public void shutdown() {
        listenersQueue.clear();
    }
}
