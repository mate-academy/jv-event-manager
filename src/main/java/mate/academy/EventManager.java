package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventManager {
    private static final int THREAD_POOL_SIZE = 10;
    private static final int SHUTDOWN_TIMEOUT = 60;
    private static final Logger logger = LogManager.getLogger(EventManager.class);
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executor.submit(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    logger.error("Failed to handle an event: " + event, e);
                }
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
                    logger.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
            logger.error("Executor was interrupted during shutdown", e);
        }
    }
}
