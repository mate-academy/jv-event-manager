package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        if (eventListeners.contains(listener)) {
            return;
        }
        eventListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        eventListeners.forEach(l -> l.onEvent(event));
    }

    public void shutdown() {
        eventListeners.clear();
    }
}
