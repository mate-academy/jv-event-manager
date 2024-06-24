package mate.academy;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventManager {
    private static final int THREAD_POOL_SIZE = 10;
    private static final Logger logger = LogManager.getLogger(Main.class);
    private final CopyOnWriteArraySet<EventListener> events;
    private final ExecutorService executor;

    public EventManager() {
        this.events = new CopyOnWriteArraySet<>();
        this.executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
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
                    logger.info("Executor did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
