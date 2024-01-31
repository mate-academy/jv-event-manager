package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final ExecutorService executorService;
    private final List<EventListener> listenersList;

    public EventManager() {
        this.executorService = Executors.newCachedThreadPool();
        this.listenersList = new CopyOnWriteArrayList<>();
    }

    public void registerListener(EventListener listener) {
        listenersList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenersList.remove(listener);
    }

    public void notifyEvent(Event event) {
        listenersList.forEach(listener ->
                executorService.submit(() -> listener.onEvent(event))
        );
    }

    public void shutdown() {
        listenersList.clear();
        executorService.shutdown();
    }
}
