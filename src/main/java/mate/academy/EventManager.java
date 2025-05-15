package mate.academy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventManager {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // Реєстрація слухача
    public void registerListener(EventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    // Скасування реєстрації слухача
    public void deregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    // Асинхронне сповіщення про подію
    public void notifyEvent(Event event) {
        for (EventListener listener : listeners) {
            executorService.submit(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    // Обробка винятків у слухачах
                    System.err.println("Error while notifying listener: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    // Завершення роботи виконавчої служби
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
