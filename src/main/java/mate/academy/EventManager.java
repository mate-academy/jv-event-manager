package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private List<EventListener> eventListenerList = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        eventListenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventListenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : eventListenerList) {
            executorService.execute(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
        eventListenerList.clear();
    }
}
