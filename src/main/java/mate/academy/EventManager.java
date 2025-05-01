package mate.academy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EventManager {
    private final ConcurrentHashMap<Integer, EventListener> listeners = new ConcurrentHashMap<>();

    public void registerListener(EventListener listener) {
        listeners.put(listener.hashCode(), listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener.hashCode());
    }

    public void notifyEvent(Event event) {
        List<Future> futures = new ArrayList<>();
        for (EventListener listener : listeners.values()) {
            futures.add(CompletableFuture.runAsync(() -> listener.onEvent(event)));
        }
        try {
            for (Future future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        listeners.clear();
    }
}
