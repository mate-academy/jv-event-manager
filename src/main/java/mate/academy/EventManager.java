package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> list = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        list.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        list.remove(listener);

    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : list) {
            executorService.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
        list.clear();
    }
}
