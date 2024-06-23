package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;

public class EventManager {
    private ConcurrentLinkedQueue<EventListener> queueu;

    public EventManager() {
        this.queueu = new ConcurrentLinkedQueue<>();
    }

    public void registerListener(EventListener listener) {
        queueu.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        queueu.remove(listener);
    }

    public void notifyEvent(Event event) {
        queueu.forEach(eventListener -> eventListener.onEvent(event));

    }

    public void shutdown() {
        queueu.clear();
    }
}
