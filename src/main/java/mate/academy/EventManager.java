package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private CopyOnWriteArrayList<EventListener> listenersList = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public EventManager() {
    }

    public void registerListener(EventListener listener) {
        listenersList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenersList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : listenersList) {
            executor.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
