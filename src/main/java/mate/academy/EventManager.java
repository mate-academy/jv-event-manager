package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {

    private final CopyOnWriteArrayList<EventListener> list = new CopyOnWriteArrayList<>();
    private final ExecutorService service = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        list.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        list.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : list) {
            service.execute(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        service.shutdown();
    }
}
