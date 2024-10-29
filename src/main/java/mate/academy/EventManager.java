package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService;

    public EventManager() {
        executorService = ForkJoinPool.commonPool();
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        CompletableFuture.runAsync(() -> {
            for (EventListener listener : listeners) {
                listener.onEvent(event);
            }
        }, executorService).thenRun(() -> {
            for (EventListener listener : listeners) {
                System.out.println(listener.getProcessedEvent());
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
