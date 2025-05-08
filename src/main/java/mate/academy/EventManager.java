package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private static final int THREAD_POOL_SIZE = 1;
    private static final int SHUTDOWN_TIMEOUT_MINUTES = 2;
    private final ThreadPoolExecutor executor;
    private final Set<EventListener> listeners;

    public EventManager() {
        this.executor = new ThreadPoolExecutor(
                THREAD_POOL_SIZE,
                THREAD_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
        this.listeners = new CopyOnWriteArraySet<>();
    }

    public void registerListener(EventListener listener) {
        if (executor.isShutdown()) {
            listeners.clear();
            return;
        }
        listeners.add(listener);
        setUpNewThreadPoolSize();
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
        setUpNewThreadPoolSize();
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void setUpNewThreadPoolSize() {
        int newSize = Math.max(listeners.size(), THREAD_POOL_SIZE);
        if (executor.getPoolSize() != newSize) {
            executor.setCorePoolSize(newSize);
            executor.setMaximumPoolSize(newSize);
        }
    }
}
