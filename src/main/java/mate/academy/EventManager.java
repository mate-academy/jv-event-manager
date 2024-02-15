package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        listeners.forEach(l -> {
            executorService.submit(() -> l.onEvent(event));
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
