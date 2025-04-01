package mate.academy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class EventManager {
    private final BlockingQueue<EventListener> queue;
    private final ExecutorService executor;

    public EventManager() {
        queue = new LinkedBlockingQueue<>();
        executor = ForkJoinPool.commonPool();
    }

    public void registerListener(EventListener listener) {
        queue.add(listener);

    }

    public void deregisterListener(EventListener listener) {
        queue.remove(listener);
    }

    public void notifyEvent(Event event) {
        queue.forEach(eventListener -> eventListener.onEvent(event));
    }

    public void shutdown() {
        executor.shutdown();
    }
}
