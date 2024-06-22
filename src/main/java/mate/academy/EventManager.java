package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        executorService.submit(() -> {
            for (EventListener listener : listeners) {
                listener.onEvent(event);
            }
        });
    }

    public void shutdown() {
        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
