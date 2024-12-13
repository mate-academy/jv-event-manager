package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> eventsList = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        eventsList.addIfAbsent(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventsList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : eventsList) {
            executor.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
