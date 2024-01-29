package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> eventListenerList = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void registerListener(EventListener listener) {
        eventListenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        eventListenerList.forEach(eventListener -> CompletableFuture
                .runAsync(() -> eventListener.onEvent(event), executorService));
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
