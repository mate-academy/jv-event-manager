package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private CopyOnWriteArrayList<EventListener> events = new CopyOnWriteArrayList<>();
    private ExecutorService service = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        events.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        events.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener even: events) {
            CompletableFuture.runAsync(() -> even.onEvent(event), service);
        }
    }

    public void shutdown() {
        service.shutdown();
    }
}
