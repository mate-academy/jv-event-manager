package mate.academy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ConcurrentHashMap<String, Event> events = new ConcurrentHashMap<>();

    private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        events.put((String) event.source(), event);
        if (!listeners.isEmpty()) {
            for (EventListener listener : listeners) {
                executorService.submit(() -> listener.onEvent(event));
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
