package mate.academy;

import java.util.ArrayList;

public class EventManager {
    private final ArrayList<EventListener> events = new ArrayList<>();

    public void registerListener(EventListener listener) {
        events.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        events.remove(listener);
    }

    public void notifyEvent(Event event) {
        events.forEach(listener -> listener.onEvent(event));
    }

    public void shutdown() {
        events.clear();
    }
}
