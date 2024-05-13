package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> data;
    private final ExecutorService executorService;

    public EventManager() {
        data = new CopyOnWriteArrayList<>();
        executorService = Executors.newCachedThreadPool();
    }

    public void registerListener(EventListener listener) {
        data.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        data.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener datum : data) {
            executorService.submit(() -> {
                datum.onEvent(event);
            });
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
