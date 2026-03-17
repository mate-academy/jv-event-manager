package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;

public class EventManager {
    private final ConcurrentLinkedQueue<EventListener> listeners = new ConcurrentLinkedQueue<>();

    public void registerListener(EventListener listener) {
        listeners.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.poll();
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void shutdown() {
        listeners.clear();
    }
}
