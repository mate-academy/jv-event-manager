package mate.academy;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private final CopyOnWriteArrayList<EventListener> list = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        list.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        list.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener listener : list) {
            executor.submit(() -> listener.onEvent(event), executor);
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                    System.err.println("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
