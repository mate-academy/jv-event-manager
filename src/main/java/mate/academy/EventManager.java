package mate.academy;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventManager {
    private static final int THREAD_POOL_SIZE = 5;
    private static final Logger logger = LogManager.getLogger(EventManager.class);
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (var listener : listeners) {
            CompletableFuture.runAsync(() -> listener.onEvent(event), executor)
                    .exceptionally(ex -> {
                        logger.error("Failed to handle an event: " + event, ex);
                        return null;
                    });
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            logger.error("Executor was interrupted during shutdown", e);
        }
    }
}
