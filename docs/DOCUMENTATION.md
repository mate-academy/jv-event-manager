# Documentation

## Overview
The `jv-event-manager` library provides a thread-safe, asynchronous event notification system that decoupling event publishers from event listeners.

## Components

### 1. `Event`
Encapsulates details of an event:
- `type`: String type/name of the event (e.g. `"UserLogin"`).
- `source`: The object representing the source/publisher of the event.

### 2. `EventListener`
An interface implemented by consumers wishing to receive events:
- `void onEvent(Event e)`: Called when an event is dispatched.
- `Event getProcessedEvent()`: Returns the last processed event for testing or monitoring purposes.

### 3. `EventManager`
The central manager coordinating registration and asynchronous event dispatching:
- `registerListener(EventListener listener)`: Thread-safe registration of a new listener.
- `deregisterListener(EventListener listener)`: Thread-safe deregistration.
- `notifyEvent(Event event)`: Asynchronously notifies all currently registered listeners of the event using a background thread pool.
- `shutdown()`: Gracefully shuts down the executor, allowing up to 60 seconds for active event tasks to complete.

## Design Decisions
- **Thread-Safety:** `CopyOnWriteArrayList` was chosen for the listener list to prevent `ConcurrentModificationException` when listeners are registered or deregistered concurrently while an event is being dispatched.
- **Asynchronous Execution:** By executing `onEvent` calls on a pool of threads (`ExecutorService`), we ensure that slow or blocking listeners do not block the event publisher thread.
- **ThreadPool Dimensioning:** The thread pool size is fixed to the number of available CPU cores, providing optimal balance between resource utilization and concurrency.
