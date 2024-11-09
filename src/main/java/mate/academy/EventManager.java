package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> eventListeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    public EventManager() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public EventManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void registerListener(EventListener listener) {
        if (!isShutdown.get()) {
            eventListeners.addIfAbsent(listener);
        } else {
            throw new IllegalStateException("EventManager is closed");
        }
    }

    public void deregisterListener(EventListener listener) {
        if (!isShutdown.get()) {
            eventListeners.remove(listener);
        } else {
            throw new IllegalStateException("EventManager is closed");
        }
    }

    public void notifyEvent(Event event) {
        if (!isShutdown.get()) {
            for (EventListener listener : eventListeners) {
                executorService.submit(() -> listener.onEvent(event));
            }
        } else {
            throw new IllegalStateException("EventManager is closed");
        }
    }

    public void shutdown() {
        if (isShutdown.compareAndSet(false, true)) {
            executorService.shutdown();
            eventListeners.clear();
        }
    }
}
