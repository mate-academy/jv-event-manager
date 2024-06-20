package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> queue = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        queue.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        queue.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : queue) {
            listener.onEvent(event);
        }
    }

    public void shutdown() {
        System.out.println("shutdown");
    }
}
