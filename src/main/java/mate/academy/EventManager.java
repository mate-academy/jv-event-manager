package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : eventListeners) {
            executorService.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        eventListeners.clear();
        executorService.shutdown();
    }
}
