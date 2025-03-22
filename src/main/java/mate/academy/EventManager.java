package mate.academy;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventManager {
    private final Queue<Event> queue = new ConcurrentLinkedQueue<>();
    private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService dispatcher = Executors.newSingleThreadExecutor();
    private final AtomicBoolean running = new AtomicBoolean(true);

    public EventManager() {
        dispatcher.submit(this::dispatchLoop);
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        queue.offer(event);
    }

    private void dispatchLoop() {
        while (running.get() || !queue.isEmpty()) {
            Event event = queue.poll();
            if (event != null) {
                for (EventListener listener : listeners) {
                    listener.onEvent(event);
                }
            }
        }
    }

    public void shutdown() {
        running.set(false);
        dispatcher.shutdown();
    }
}
