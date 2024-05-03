package mate.academy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventManager {

    private final List<EventListener> managedListeners = new ArrayList<>();

    public void registerListener(EventListener listener) {
        managedListeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        managedListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        managedListeners.forEach(
                listener -> CompletableFuture.runAsync(() -> listener.onEvent(event))
        );
    }

    public void shutdown() {

    }
}
