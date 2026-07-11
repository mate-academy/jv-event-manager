package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {

    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executor;

    public EventManager() {
        this.executor = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            return;
        }

        for (EventListener listener : listeners) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
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
