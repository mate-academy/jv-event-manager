package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    public static final int NUMBER_OF_THREADS = 4;
    private final ExecutorService executorService;
    private final List<EventListener> eventListeners;

    public EventManager() {
        executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        eventListeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : eventListeners) {
            CompletableFuture.runAsync(() -> eventListener.onEvent(event), executorService);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
