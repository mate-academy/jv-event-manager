package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(50);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            try {
                executor.execute(() -> listener.onEvent(event));
            } catch (Exception ex) {
                throw new RuntimeException("Error notifying listener: " + listener, ex);
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
