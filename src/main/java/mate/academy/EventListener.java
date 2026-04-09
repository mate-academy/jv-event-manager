package mate.academy;

public interface EventListener {
    void onEvent(Event e);

    Event getProcessedEvent();

}
