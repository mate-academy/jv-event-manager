package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;

public class EventManager {
    private final ConcurrentLinkedQueue<EventListener> listeners = new ConcurrentLinkedQueue<>();

    public void registerListener(final EventListener listener) {
        listeners.offer(listener);
    }

    public void deregisterListener(final EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(final Event event) {
        for (final EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void shutdown() {
        listeners.clear();
    }
}
