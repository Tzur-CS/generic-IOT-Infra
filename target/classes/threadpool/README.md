# ThreadPool Project

## Overview
This project is a custom implementation of a **Thread Pool** in Java using a **priority-based task queue**. It allows submitting tasks with different priority levels and provides functionality to manage the number of worker threads dynamically.

## Features
- Supports **priority-based task execution** (LOW, DEFAULT, HIGH).
- Allows **dynamic adjustment of worker threads**.
- Provides methods to **pause and resume** task execution.
- Supports **graceful shutdown and termination**.
- Uses **Java Concurrency utilities** (Semaphore, Future, Lock, etc.).

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

## Usage
### Creating a ThreadPool
```java
ThreadPool threadPool = new ThreadPool(5); // Creates a pool with 5 threads
```

### Submitting Tasks
#### Runnable Task
```java
threadPool.submit(() -> System.out.println("Task executed"), Priority.DEFAULT);
```

#### Callable Task
```java
Future<Integer> future = threadPool.submit(() -> 42, Priority.HIGH);
System.out.println("Result: " + future.get());
```

### Changing Thread Pool Size
```java
threadPool.setNumOfThreads(10); // Increases worker threads to 10
```

### Pausing and Resuming
```java
threadPool.pauseTP();   // Pauses execution
threadPool.resumeTP();  // Resumes execution
```

### Shutting Down
```java
threadPool.shutDown();
threadPool.awaitTermination(); // Waits for all tasks to complete
```

## Running Tests
This project includes **JUnit tests** to verify functionality.

### Running Tests with Maven
```bash
mvn test
```

### Example Test Case
```java
@Test
public void testSubmitRunnable() throws ExecutionException, InterruptedException {
    Future<Void> future = threadPool.submit(() -> {
        System.out.println("Task executed");
        return null;
    }, Priority.DEFAULT);
    assertNull(future.get());
    assertTrue(future.isDone());
}
```

## File Structure
```
project-root/
│── src/main/java/threadpool/
│   ├── ThreadPool.java        # Main ThreadPool implementation
│   ├── Priority.java          # Enum for priority levels
│── src/test/java/threadpool/
│   ├── ThreadPoolTest.java    # JUnit test cases
│── pom.xml                    # Maven configuration file
│── README.md                  # Project documentation
```

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Make changes and commit (`git commit -m 'Added new feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Create a **Pull Request**.

## License
This project is licensed under the **MIT License**.

