package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final CopyOnWriteArraySet<EventListener> setOfSubs = new CopyOnWriteArraySet<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        setOfSubs.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        setOfSubs.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        CompletableFuture<?>[] futures = setOfSubs.stream()
                .map(listener -> CompletableFuture.runAsync(() -> {
                    try {
                        listener.onEvent(event);
                    } catch (Exception e) {
                        throw new RuntimeException("Error in listener: " + e.getMessage(), e);
                    }
                }, executor))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
    }

    public void shutdown() {
        setOfSubs.clear();
        executor.shutdown();
    }
}
