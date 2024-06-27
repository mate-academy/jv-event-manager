package mate.academy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManager {
    private static final int TIMEOUT = 5;
    private static final int MAX_CONCURRENT_TASKS = 10;
    private static final int QUEUE_CAPACITY = 50;
    private static final Logger LOGGER = Logger.getLogger(EventManager.class.getName());

    private final ConcurrentHashMap.KeySetView<EventListener,
            Boolean> listeners = ConcurrentHashMap.newKeySet();
    private final ExecutorService executorService;

    public EventManager() {
        this.executorService = new ThreadPoolExecutor(
                MAX_CONCURRENT_TASKS,
                MAX_CONCURRENT_TASKS,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executorService.submit(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    throw new RuntimeException("Error while notifying listener: " + listener, e);
                }
            });
        }
    }

    public void shutdown() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                    LOGGER.log(Level.SEVERE, "Executor did not terminate after shutdownNow.");
                }
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Shutdown interrupted", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
