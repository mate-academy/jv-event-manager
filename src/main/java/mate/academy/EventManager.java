package mate.academy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private static final int AWAIT_TERMINATION_TIMEOUT = 60;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final ConcurrentHashMap<EventListener, Boolean> listeners = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        listeners.put(listener, true);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners.keySet()) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(AWAIT_TERMINATION_TIMEOUT, TIME_UNIT)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
