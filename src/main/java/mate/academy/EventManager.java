package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {

    private final ConcurrentLinkedQueue<EventListener> listeners = new ConcurrentLinkedQueue<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        listeners.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        listeners.forEach(listener -> executorService.submit(() -> listener.onEvent(event)));
    }

    public void shutdown() {
        listeners.clear();
        executorService.shutdown();
    }
}
