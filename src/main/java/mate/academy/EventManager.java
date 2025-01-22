package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> listenerList = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null. ");
        }
        listenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        for (EventListener listener : listenerList) {
            executorService.submit(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    System.err.println("Error notifying listener: " + e.getMessage());
                }
            });
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
