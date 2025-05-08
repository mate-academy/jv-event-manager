package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private List<EventListener> listenersList = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        listenersList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenersList.remove(listener);
    }

    public void notifyEvent(Event event) {
        listenersList.forEach(listener -> executor.submit(() -> listener.onEvent(event)));
    }

    public void shutdown() {
        executor.shutdown();
    }
}
