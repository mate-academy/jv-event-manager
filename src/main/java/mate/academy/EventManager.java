package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<EventListener> data;

    public EventManager() {
        data = new CopyOnWriteArrayList<>();
    }

    public void registerListener(EventListener listener) {
        data.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        data.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener datum : data) {
            datum.onEvent(event);
        }
    }

    public void shutdown() {
    }
}
