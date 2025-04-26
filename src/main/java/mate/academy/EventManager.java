package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<EventListener> list = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        list.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        list.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : list) {
            eventListener.onEvent(event);
        }
    }

    public void shutdown() {
        list.clear();
    }
}
