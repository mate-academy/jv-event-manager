package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> eventListeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService;

    public EventManager() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public EventManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void registerListener(EventListener listener) {
        eventListeners.addIfAbsent(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : eventListeners) {
            executorService.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
        eventListeners.clear();
    }
}
