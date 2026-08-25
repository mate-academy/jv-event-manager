package mate.academy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        this.listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        this.listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        List<CompletableFuture<Void>> futures = this.listeners.stream()
                .map(listener ->
                    CompletableFuture.runAsync(() -> listener.onEvent(event), executor)
                        .exceptionally(ex -> {
                            System.out.println(listener.getClass().getSimpleName()
                                    + " failed on event " + event + "\n" + ex.getMessage());

                            return null;
                        })
            )
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    }

    public void shutdown() {

        this.executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                this.executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executor.shutdownNow();
            Thread.currentThread().interrupt();

        }
    }
}
