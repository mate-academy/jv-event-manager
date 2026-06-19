# Project Context: jv-event-manager

## Overview
This project contains a thread-safe asynchronous `EventManager` implementation designed to handle registering, deregistering, and asynchronously notifying multiple `EventListener` instances of `Event` objects in a concurrent environment.

## Current State
- The implementation of `EventManager` is complete, thread-safe, and documented with clear inline comments.
- It uses a `CopyOnWriteArrayList` for thread-safe listener management.
- It uses a fixed-size `ExecutorService` (based on available processors) to handle event notifications asynchronously.
- The project successfully compiles and passes all unit tests, including high-volume concurrent event tests.
- Checkstyle validation has 0 violations.

## Recent Changes
- Added clear inline comments to [EventManager.java](file:///Users/alex/IdeaProjects/jv-event-manager/src/main/java/mate/academy/EventManager.java) explaining how the thread pool and thread-safe collections operate.

## Pending Items / Next Steps
- Review with the user if any additional features (e.g., custom error handling when listener execution fails, configurable thread pools, or listener priorities) are desired.
