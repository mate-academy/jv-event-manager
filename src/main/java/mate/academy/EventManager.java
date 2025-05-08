package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private List<EventListener> listenersQueue = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        listenersQueue.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenersQueue.remove(listener);
    }

    public void notifyEvent(Event event) {
        listenersQueue.forEach(listener -> executorService.submit(
                () -> listener.onEvent(event)));
    }

    public void shutdown() {
        listenersQueue.clear();
        executorService.shutdown();
    }
}
