package mate.academy;

import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private final ConcurrentHashMap<Integer, EventListener> listeners = new ConcurrentHashMap<>();

    public void registerListener(EventListener listener) {
        listeners.put(listener.hashCode(), listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener.hashCode());
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners.values()) {
            listener.onEvent(event);
        }
    }

    public void shutdown() {
        listeners.clear();
    }
}
