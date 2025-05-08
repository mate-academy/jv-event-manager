package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private List<EventListener> list = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        list.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        list.remove(listener);
    }

    public void notifyEvent(Event event) {
        list.forEach(notification -> {
            executorService.submit(() -> notification.onEvent(event));
        });

    }

    public void shutdown() {
        executorService.shutdown();
    }
}
