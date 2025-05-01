package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private ExecutorService executor = Executors.newCachedThreadPool();
    private List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
        getProcessedEventFromListenersList();
    }

    public void shutdown() {
        executor.shutdown();
    }

    private void getProcessedEventFromListenersList() {
        for (EventListener listener : listeners) {
            CompletableFuture.runAsync(listener::getProcessedEvent,
                    executor).exceptionally(this::handleException);
        }
    }

    private Void handleException(Throwable throwable) {
        throw new RuntimeException("Error while processing event", throwable);
    }
}
