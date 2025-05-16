package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private List<EventListener> listenerList = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        listenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listenerList) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
