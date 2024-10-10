package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<EventListener> listenerList = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listenerList) {
            listener.onEvent(event);
        }
    }

    public void shutdown() {

    }
}
