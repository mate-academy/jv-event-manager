package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        eventListeners.forEach(listener -> executor.submit(() -> listener.onEvent(event)));
    }

    public void shutdown() {
        executor.shutdown();
    }
}