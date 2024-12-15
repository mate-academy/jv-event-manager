package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> events = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        events.addIfAbsent(listener);
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
    }
}
