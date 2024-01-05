package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private List<EventListener> listenersQueue = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listenersQueue.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenersQueue.remove(listener);
    }

    public void notifyEvent(Event event) {
        listenersQueue.forEach(listener -> listener.onEvent(event));
    }

    public void shutdown() {

    }
}
