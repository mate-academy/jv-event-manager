package mate.academy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventManager {

    private final ConcurrentMap<Integer, EventListener> collection = new ConcurrentHashMap<>();

    public void registerListener(EventListener listener) {
        collection.put(listener.hashCode(), listener);
    }

    public void deregisterListener(EventListener listener) {
        collection.remove(listener.hashCode());
    }

    public void notifyEvent(Event event) {
        collection.values().forEach(listener -> listener.onEvent(event));
    }

    public void shutdown() {
        collection.clear();
    }
}
