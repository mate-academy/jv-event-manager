package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private List<EventListener> eventList = new CopyOnWriteArrayList<>();

    public void registerListener(EventListener listener) {
        eventList.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        eventList.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (EventListener eventListener : eventList) {
            executorService.submit(() -> eventListener.onEvent(event));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
