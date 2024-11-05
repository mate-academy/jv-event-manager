package mate.academy;

import mate.academy.listeners.LoggingListener;
import mate.academy.listeners.SampleListener;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        EventManager manager = new EventManager();

        // Register listeners
        EventListener sampleListener = new SampleListener();
        System.out.println("🎉 SampleListener has joined the party!");
        manager.registerListener(sampleListener);

        EventListener loggingListener = new LoggingListener();
        System.out.println("📢 LoggingListener is now on duty!");
        manager.registerListener(loggingListener);

        // Simulate events immediately after declaration
        final Event loginEvent = new Event("UserLogin", "User123");
        System.out.println("\n🔔 Notifying events sequentially:");
        manager.notifyEvent(loginEvent);
        System.out.println("🔑 User login event sent!");

        final Event dataChangeEvent = new Event("DataChange", "Database");
        manager.notifyEvent(dataChangeEvent);
        System.out.println("🗃️ Data change event sent!");

        final Event logoutEvent = new Event("UserLogout", "User123");
        manager.notifyEvent(logoutEvent);
        System.out.println("🚪 User logout event sent!");

        // Start multiple threads to send events
        System.out.println("\n🌟 Let’s get wild! Starting multiple threads to fire up events:");
        for (int i = 0; i < 3; i++) {
            int threadId = i + 1;
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    Event event = new Event("ThreadEvent" + threadId, "Source" + j);
                    System.out.println("💥 Thread " + threadId + " launching event rocket: "
                            + event.type());
                    manager.notifyEvent(event);
                    try {
                        Thread.sleep(100); // Delay for visibility
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        // Delay to allow threads to finish before shutting down the event manager
        Thread.sleep(2000);

        // Shut down the EventManager with a farewell message
        System.out.println("\n🛑 The party is over! Shutting down the EventManager...");
        manager.shutdown();
        System.out.println("🎬 EventManager has gracefully exited. Until next time!");
    }
}
