package mate.academy;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Map<EventListener, ExecutorService> listenerExecutors = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void registerListener(EventListener listener) {
        listenerExecutors.put(listener, executorService);
    }

    public void deregisterListener(EventListener listener) {
        listenerExecutors.remove(listener);
    }

    public void notifyEvent(Event event) {
        Set<Map.Entry<EventListener, ExecutorService>> entries = listenerExecutors.entrySet();
        for (Map.Entry<EventListener, ExecutorService> entry : entries) {
            ExecutorService executorService = entry.getValue();
            EventListener listener = entry.getKey();
            CompletableFuture.runAsync(() -> {
                listener.onEvent(event);
                listener.getProcessedEvent();
            }, executorService);
        }
    }

    public void shutdown() {
        listenerExecutors.values().forEach(ExecutorService::shutdown);
        executorService.shutdown();
    }
}
