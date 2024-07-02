package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManager {
    private static final Logger LOGGER = Logger.getLogger(EventManager.class.getName());
    private static final int THREAD_POOL_SIZE = 10;
    private static final int AWAIT_TERMINATION_TIMEOUT = 60;
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            try {
                executorService.submit(() -> listener.onEvent(event));
            } catch (RejectedExecutionException e) {
                LOGGER.log(Level.WARNING, "Event: " + event
                        + " was not handled by listeners, "
                        + EventManager.class.getSimpleName()
                        + " is shut down, or in the process of it.", e);
            }
        }
    }

    public void shutdown() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(AWAIT_TERMINATION_TIMEOUT, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Shutdown interrupted: ", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
