# ThreadPool Project

## Overview
This project is a custom implementation of a **Thread Pool** in Java using a **priority-based task queue**. It allows submitting tasks with different priority levels and provides functionality to manage the number of worker threads dynamically.

## Features
- Supports **priority-based task execution** (LOW, DEFAULT, HIGH).
- Allows **dynamic adjustment of worker threads**.
- Provides methods to **pause and resume** task execution.
- Supports **graceful shutdown and termination**.
- Implements different waitable priority queue mechanisms.
- Uses **Java Concurrency utilities** (Semaphore, Lock, Condition, Future, etc.).

## Installation & Setup
### Prerequisites
- **Java 8 or later** (Recommended: Java 21)
- **Maven** (for dependency management and testing)
- **IntelliJ IDEA** (optional, but recommended for development)

### Cloning the Repository
```bash
git clone <repository-url>
cd <project-folder>
```

### Importing into IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Click **File** → **Open**.
3. Select the project directory.
4. IntelliJ will automatically detect and set up Maven.
5. Wait for dependencies to download.

## Waitable Priority Queues
This project includes different implementations of **Waitable Priority Queues (WaitablePQ)**:
- `WaitablePQSem` - Uses **Semaphores** for synchronization.
- `WaitablePQFixedSize` - A bounded priority queue using **Semaphores**.
- `WaitablePQCond` - Uses **Locks and Conditions** for synchronization.

## Running Tests
This project includes **JUnit tests** to verify functionality.

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Make changes and commit (`git commit -m 'Added new feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Create a **Pull Request**.


