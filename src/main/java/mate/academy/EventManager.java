package mate.academy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private boolean isShutdown = false;

    public void registerListener(EventListener listener) {
        if (isShutdown) {
            throw new IllegalStateException("Cannot register listener after shutdown");
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : new CopyOnWriteArrayList<>(listeners)) {
            listener.onEvent(event);
        }
    }

    public void shutdown() {
        listeners.clear();
        isShutdown = true;
    }
}
