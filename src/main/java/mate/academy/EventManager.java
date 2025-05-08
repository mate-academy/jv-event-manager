package mate.academy;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventManager {
    private final ConcurrentLinkedQueue<EventListener> listeners = new ConcurrentLinkedQueue<>();
    private final ExecutorService executorService;

    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    public EventManager() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        if (!isShutdown.get()) {
            listeners.add(listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (isShutdown.get()) {
            throw new IllegalStateException("EventManager is shutdown");
        }

        for (EventListener listener : listeners) {
            executorService.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        isShutdown.set(true);
        executorService.shutdown();
    }
}
