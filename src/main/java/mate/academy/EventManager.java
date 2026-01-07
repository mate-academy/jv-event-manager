package mate.academy;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private ConcurrentLinkedDeque<EventListener> listeners = new ConcurrentLinkedDeque<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        this.listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        this.listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener: this.listeners) {
            executorService.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
        this.listeners.clear();
    }
}
