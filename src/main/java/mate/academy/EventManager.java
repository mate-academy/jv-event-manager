package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        eventListeners.forEach(eventListener -> CompletableFuture
                .runAsync(() -> eventListener.onEvent(event)));
    }

    public void shutdown() {
        eventListeners.clear();
    }
}
