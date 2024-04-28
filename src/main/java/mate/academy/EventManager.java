package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> listeners;

    public EventManager() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            Thread thread = new Thread(() -> listener.onEvent(event));
            thread.start();
        }

    }

    public void shutdown() {

    }
}
