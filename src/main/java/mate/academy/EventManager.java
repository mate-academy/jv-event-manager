package mate.academy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ExecutorService executorService;
    private final Set<EventListener> eventListenerSet;

    public EventManager() {
        executorService = Executors.newCachedThreadPool();
        eventListenerSet = new CopyOnWriteArraySet<>();
    }

    public void registerListener(EventListener listener) {
        eventListenerSet.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListenerSet.remove(listener);
    }

    public void notifyEvent(Event event) {
        eventListenerSet
                    .forEach(listener -> executorService.execute(() -> listener.onEvent(event)));
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
