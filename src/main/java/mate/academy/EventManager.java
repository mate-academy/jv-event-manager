package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> listeners
            = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        listeners.forEach(l -> l.onEvent(event));
    }

    public void shutdown() {
        listeners.clear();
    }
}
