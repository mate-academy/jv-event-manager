package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private volatile boolean isShoutDown;

    public EventManager() {
        this.isShoutDown = false;
    }

    public void registerListener(EventListener listener) {
        if (isShoutDown) {
            return;
        }
        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        if (isShoutDown) {
            return;
        }
        eventListeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (isShoutDown) {
            return;
        }
        for (EventListener eventListener: eventListeners) {
            executorService.submit(() -> {
                try {
                    eventListener.onEvent(event);
                } catch (Exception e) {
                    System.out.println("Can't notify listener: " + eventListener.toString());
                }

            });
        }
    }

    public void shutdown() {
        this.isShoutDown = true;
        executorService.shutdown();
    }
}
