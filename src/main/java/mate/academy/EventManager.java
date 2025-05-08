package mate.academy;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private Set<EventListener> listeners = ConcurrentHashMap.newKeySet();
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener: listeners) {
            CompletableFuture.runAsync(() -> eventListener.onEvent(event), executor);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
