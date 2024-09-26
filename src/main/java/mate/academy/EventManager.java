package mate.academy;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private static final Set<EventListener> eventListeners = ConcurrentHashMap.newKeySet();

    public void registerListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (eventListeners.iterator().hasNext()) {
            eventListeners.iterator().next().onEvent(event);
        }
    }

    public void shutdown() {
        eventListeners.clear();
    }
}
