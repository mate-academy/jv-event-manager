package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private ConcurrentLinkedQueue<EventListener> queue;

    public EventManager() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void registerListener(EventListener listener) {
        queue.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        queue.remove(listener);
    }

    public void notifyEvent(Event event) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        queue.forEach(eventListener ->
                CompletableFuture.runAsync(() -> eventListener.onEvent(event), executorService));
        executorService.shutdown();
    }

    public void shutdown() {
        queue.clear();
    }
}
