package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> listeners;
    private final ExecutorService executorService;

    public EventManager() {
        listeners = new CopyOnWriteArrayList<>();
        executorService = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        listeners.forEach(listener -> executorService.execute(() -> listener.onEvent(event)));
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
