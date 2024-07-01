package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private static final int USERS_COUNT = 10;
    private static final int TIMEOUT_SECONDS = 10;
    private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(USERS_COUNT);

    public void registerListener(EventListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            try {
                executorService.submit(() -> listener.onEvent(event));
            } catch (RejectedExecutionException e) {
                System.err.println("Task rejected for listener: " + listener);
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
