package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> eventListenerList = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public void registerListener(EventListener listener) {
        eventListenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : eventListenerList) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        eventListenerList.clear();
        executor.shutdown();
    }
}
