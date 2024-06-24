package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> events;
    private final ExecutorService executor;

    public EventManager() {
        this.events = new CopyOnWriteArrayList<>();
        this.executor = Executors.newScheduledThreadPool(10);
    }

    public void registerListener(EventListener listener) {
        events.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        events.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : events) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(500, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
