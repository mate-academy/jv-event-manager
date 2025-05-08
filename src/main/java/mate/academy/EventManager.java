package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        if (listener == null) {
            throw new RuntimeException("null not accepted");
        }
        if (listeners.contains(listener)) {
            throw new RuntimeException("listner alread registered");
        }

        listeners.add(listener);

    }

    public void deregisterListener(EventListener listener) {
        if (listener == null) {
            throw new RuntimeException("null not accepted");
        }
        if (!listeners.contains(listener)) {
            throw new RuntimeException("listner is not registered");
        }

        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executorService.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
