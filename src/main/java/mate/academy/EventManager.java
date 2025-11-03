package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> listeners;
    private final ExecutorService executorService;
    private volatile boolean isShutdown;

    public EventManager() {
        this.listeners = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.isShutdown = false;
    }

    public void registerListener(EventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        if (isShutdown) {
            throw new IllegalStateException("EventManager is shutdown");
        }
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (isShutdown) {
            throw new IllegalStateException("EventManager is shutdown");
        }

        for (EventListener listener : listeners) {
            executorService.submit(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    System.err.println("Error notifying listener: " + e.getMessage());
                }
            });
        }
    }

    public void shutdown() {
        isShutdown = true;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int getListenerCount() {
        return listeners.size();
    }

    public boolean isShutdown() {
        return isShutdown;
    }
}
