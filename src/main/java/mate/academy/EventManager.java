package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private static final List<EventListener> EVENT_LISTENERS = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        Thread registerListener = new Thread(() -> EVENT_LISTENERS.add(listener));
        registerListener.start();
        try {
            registerListener.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't join registerListener", e);
        }
    }

    public void deregisterListener(EventListener listener) {
        Thread deregisterListener = new Thread(() -> EVENT_LISTENERS.remove(listener));
        deregisterListener.start();
        try {
            deregisterListener.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't join deregisterListener", e);
        }
    }

    public void notifyEvent(Event event) {
        Thread notifyEvent = new Thread(() -> EVENT_LISTENERS.forEach(l -> l.onEvent(event)));
        notifyEvent.start();
        try {
            notifyEvent.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't join notifyEvent", e);
        }
    }

    public void shutdown() {
    }
}
