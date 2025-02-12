package mate.academy;

import java.util.concurrent.CopyOnWriteArraySet;

public class EventManager {
    private CopyOnWriteArraySet setOfSubs = new CopyOnWriteArraySet();

    public void registerListener(EventListener listener) {
        setOfSubs.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        setOfSubs.remove(listener);
    }

    public void notifyEvent(Event event) {
        for (Object setOfSub : setOfSubs) {
            EventListener listener = (EventListener) setOfSub;
            listener.onEvent(event);
        }
    }

    public void shutdown() {
        setOfSubs.clear();
    }
}
