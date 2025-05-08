package mate.academy;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManager {
    private static final Logger logger = Logger.getLogger(EventManager.class.getName());
    private final Set<EventListener> listeners;
    private final ExecutorService executorService;

    public EventManager() {
        this.listeners = new CopyOnWriteArraySet<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            CompletableFuture.runAsync(()
                            -> listener.onEvent(event), executorService)
                    .exceptionally(ex -> {
                        logger.log(Level.SEVERE,
                                "Exception while notifying listener: "
                                        + listener, ex);
                        return null;
                    });
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.warning("ExecutorService did not terminate in the specified time.");
                List<Runnable> droppedTasks = executorService.shutdownNow();
                logger.warning("ExecutorService was abruptly shut down. "
                        + droppedTasks.size()
                        + " tasks will not be executed.");
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "ExecutorService shutdown interrupted", ex);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
