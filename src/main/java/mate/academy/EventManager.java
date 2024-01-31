package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<EventListener> listenersList = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listenersList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenersList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listenersList) {
            listener.onEvent(event);
        }
    }

    public void shutdown() {
        listenersList.clear();
    }
}
