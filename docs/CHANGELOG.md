# Changelog

## [1.0.0] - 2026-06-19

### Added
- Created a thread-safe asynchronous `EventManager` class to handle registration, deregistration, and asynchronous notification of listeners.
- Integrated a fixed-size `ExecutorService` utilizing the available CPU processors for non-blocking event dispatching.
- Used `CopyOnWriteArrayList` to ensure thread-safe listener management without explicit locks.
- Implemented a graceful shutdown sequence to allow running event dispatch tasks to complete within a 60-second timeout.
- Added comprehensive unit tests in `EventManagerTest` validating single event notifications, multi-event concurrency, listener deregistration correctness, and high-volume throughput.
- Created `docs/PROJECT_CONTEXT.md` to track current task alignment and project progress.
