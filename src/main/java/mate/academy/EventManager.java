package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ExecutorService executor;
    private final List<EventListener> listeners;

    public EventManager() {
        executor = Executors.newCachedThreadPool();
        listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);

    }

    public void notifyEvent(Event event) {
        listeners.forEach(
                listener -> executor.execute(
                        () -> listener.onEvent(event)
                )
        );

    }

    public void shutdown() {
        listeners.clear();
        executor.shutdown();
    }
}
