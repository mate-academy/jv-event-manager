package mate.academy;

import java.util.List;
import java.util.concurrent.*;

public class EventManager {
    private final ExecutorService executorService = Executors.newFixedThreadPool(6);

    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        listeners.stream()
                .map(l -> (Callable<EventListener>) () -> {
                    l.onEvent(event);
                    return l;
                })
                .forEach(executorService::submit);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
