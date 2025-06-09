package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventManager {

    private final List<EventListener> listeners;
    private final AtomicBoolean isRunning;
    private final ExecutorService executorService;

    public EventManager() {
        executorService = Executors.newCachedThreadPool();
        isRunning = new AtomicBoolean(true);
        listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(EventListener listener) {
        if (!isRunning.get()) {
            throw new IllegalStateException("EventManager is not running");
        }
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            System.out.println("Registered listener: " + listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        if (!isRunning.get()) {
            throw new IllegalStateException("EventManager is not running");
        }
        if (listener != null && listeners.contains(listener)) {
            listeners.remove(listener);
            System.out.println("Unregistered listener: " + listener);
            return;
        }
        System.out.println("Unknown listener.");
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executorService.submit(() -> listener.onEvent(event));
        }
    }

    public void shutdown() {
        isRunning.set(false);
        executorService.shutdown();
    }
}
