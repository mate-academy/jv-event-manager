package mate.academy;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Map<EventListener, ExecutorService> listenerExecutors = new ConcurrentHashMap<>();

    public void registerListener(EventListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        listenerExecutors.put(listener, executorService);
    }

    public void deregisterListener(EventListener listener) {
        ExecutorService executorService = listenerExecutors.remove(listener);
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void notifyEvent(Event event) {
        Set<Map.Entry<EventListener, ExecutorService>> entries = listenerExecutors.entrySet();
        for (Map.Entry<EventListener, ExecutorService> entry : entries) {
            ExecutorService executorService = entry.getValue();
            EventListener listener = entry.getKey();
            executorService.submit(() -> {
                listener.onEvent(event);
                listener.getProcessedEvent();
            });
        }
    }

    public void shutdown() {
        listenerExecutors.values().forEach(ExecutorService::shutdown);
        listenerExecutors.clear();
    }
}
