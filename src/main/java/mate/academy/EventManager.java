package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private CopyOnWriteArrayList<EventListener> eventListeners =
            new CopyOnWriteArrayList<>();

    private final ExecutorService executor =
            Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        eventListeners.forEach(eventListener -> executor.submit(
                () -> eventListener.onEvent(event)));
    }

    public void shutdown() {
        executor.shutdown();
    }
}
