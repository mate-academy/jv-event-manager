package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private static final int THREAD_QUANTITY = 2;
    private final List<EventListener> registeredListenerList = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_QUANTITY);


    public void registerListener(EventListener listener) {
        registeredListenerList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        registeredListenerList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : registeredListenerList) {
            executorService.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
        registeredListenerList.clear();
    }
}