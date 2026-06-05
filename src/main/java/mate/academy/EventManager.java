package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    public void registerListener(EventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void notifyEvent(Event event) {
        if (event == null || executor.isShutdown()) {
            return;
        }

        for (EventListener listener : listeners) {
            executor.execute(() -> {
                listener.onEvent(event);
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
