package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private List<EventListener> eventListenerList = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        eventListenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : eventListenerList) {
            eventListener.onEvent(event);
        }
    }

    public void shutdown() {
        eventListenerList.clear();
    }
}
