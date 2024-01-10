package mate.academy;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Queue<EventListener> listeners = new ConcurrentLinkedQueue<>();
    private final ExecutorService service = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            service.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        service.shutdown();
    }
}
