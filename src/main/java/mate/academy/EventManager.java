package mate.academy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManager {
    private final List<EventListener> listenerList = Collections.synchronizedList(
            new ArrayList<>());

    public void registerListener(EventListener listener) {
        listenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : listenerList) {
            eventListener.onEvent(event);
        }
    }

    public void shutdown() {

    }
}
