package mate.academy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class EventManager {
    private final LinkedBlockingQueue<EventListener> listeners = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        listeners.offer(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executorService.execute(() -> {
                listener.onEvent(event);
            });
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
