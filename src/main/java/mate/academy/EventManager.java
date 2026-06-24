package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        executorService.execute(() -> {
            for (EventListener listener : listeners) {
                listener.onEvent(event);
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
