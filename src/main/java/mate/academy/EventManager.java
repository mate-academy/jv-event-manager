package mate.academy;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final CopyOnWriteArraySet<EventListener> setOfSubs = new CopyOnWriteArraySet<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        setOfSubs.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        setOfSubs.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        for (EventListener listener : setOfSubs) {
            Callable<Void> task = () -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    System.err.println("Error notifying listener: " + e.getMessage());
                }
                return null;
            };
            executor.submit(task);
        }
    }

    public void shutdown() {
        setOfSubs.clear();
        executor.shutdown();
    }
}
