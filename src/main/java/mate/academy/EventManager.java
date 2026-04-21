package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventManager {

    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private static final Logger log = LoggerFactory.getLogger(EventManager.class);

    public void registerListener(EventListener listener) {

        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {

        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {

        if (executor.isShutdown()) {
            return;
        }

        for (EventListener listener : listeners) {
            submitSafely(listener, event);
        }
    }

    private void submitSafely(EventListener listener, Event event) {
        try {
            executor.submit(() -> safeNotify(listener, event));
        } catch (RejectedExecutionException exception) {
            log.error("Task rejected: executor is shutdown", exception);
        }
    }

    private void safeNotify(EventListener listener, Event event) {
        try {
            listener.onEvent(event);
        } catch (Exception exception) {
            log.error("Listener failed: " + exception.getMessage());
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
