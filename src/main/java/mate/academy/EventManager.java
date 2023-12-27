package mate.academy;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class EventManager {
    private final BlockingDeque<EventListener> queue;
    private final ExecutorService executorService;

    public EventManager() {
        this.queue = new LinkedBlockingDeque<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        queue.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        queue.removeFirstOccurrence(listener);
    }

    public void notifyEvent(Event event) {
        queue.forEach(listener ->
                executorService.submit(() ->
                        listener.onEvent(event)
                )
        );
    }

    public void shutdown() {
        queue.forEach(listener ->
                executorService.shutdown());
        queue.clear();
    }
}
