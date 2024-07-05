package mate.academy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Map<Integer, EventListener> listeners = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void registerListener(EventListener listener) {
        listeners.put(listener.hashCode(), listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener.hashCode());
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners.values()) {
            executorService.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
