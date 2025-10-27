package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    // Lista wątkowo-bezpieczna, pozwala na dodawanie/usuwanie listenerów w trakcie iteracji
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    // Executor do asynchronicznego powiadamiania listenerów
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // Rejestracja słuchacza
    public void registerListener(EventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    // Wyrejestrowanie słuchacza
    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    // Asynchroniczne powiadamianie o zdarzeniu
    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executor.submit(() -> listener.onEvent(event));
        }
    }

    // Zamykanie executor service
    public void shutdown() {
        executor.shutdown();
    }
}
