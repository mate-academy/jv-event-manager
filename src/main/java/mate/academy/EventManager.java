package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> events;
    private final ExecutorService executorService;

    public EventManager() {
        this.events = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        events.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        events.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : events) {
            executorService.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
