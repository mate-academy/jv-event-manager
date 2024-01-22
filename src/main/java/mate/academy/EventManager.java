package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventManager {
    private final ConcurrentLinkedQueue<EventListener> eventListeners =
            new ConcurrentLinkedQueue<>();

    public void registerListener(EventListener listener) {
        eventListeners.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : eventListeners) {
            CompletableFuture.runAsync(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        eventListeners.clear();
    }
}
