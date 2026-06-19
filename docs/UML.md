# UML Diagram

Below is a pseudographic UML class diagram illustrating the relationships in the event manager.

```
+--------------------------------------------------------+
|                      EventManager                      |
+--------------------------------------------------------+
| - listeners: CopyOnWriteArrayList<EventListener>       |
| - executor: ExecutorService                            |
+--------------------------------------------------------+
| + registerListener(listener: EventListener): void      |
| + deregisterListener(listener: EventListener): void    |
| + notifyEvent(event: Event): void                      |
| + shutdown(): void                                     |
+--------------------------------------------------------+
                           |
                           | manages / notifies
                           v
+--------------------------------------------------------+
|                    <<interface>>                       |
|                    EventListener                       |
+--------------------------------------------------------+
| + onEvent(e: Event): void                              |
| + getProcessedEvent(): Event                           |
+--------------------------------------------------------+
                           ^
                           | implements
             +-------------+-------------+
             |                           |
+--------------------------+   +--------------------------+
|  AbstractEventListener   |   |      SampleListener      |
+--------------------------+   +--------------------------+
| - processedEvent: Event  |   | + onEvent(e: Event): void|
+--------------------------+   +--------------------------+
             ^
             | extends
             |
+--------------------------+
|     LoggingListener      |
+--------------------------+
| + onEvent(e: Event): void|
+--------------------------+

+--------------------------------------------------------+
|                        Event                           |
|                      <<record>>                        |
+--------------------------------------------------------+
| - type: String                                         |
| - source: Object                                       |
+--------------------------------------------------------+
```
