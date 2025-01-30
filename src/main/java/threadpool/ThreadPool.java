package threadpool;

import com.sun.istack.internal.NotNull;
import waitablequeue.WaitablePQFixedSize;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool implements Executor {
    private final int MAX_PRIORITY = 10;
    private final WaitablePQFixedSize<Task<?>> pq;
    private final AtomicInteger numOfThreads;
    private final Semaphore threadsSemaphore = new Semaphore(0);
    private final Semaphore endedThreadsSem = new Semaphore(0);
    private volatile boolean isShutDown;
    private volatile boolean isStopped = false;
    private Future<Void> lastPoisonPill;
    Task<Void> PoisonAppleMax;
    Task<Void> PoisonAppleShutDown;

    public ThreadPool(int numOfThreads) {
        this.numOfThreads = new AtomicInteger(numOfThreads);
        final int GROWTH_FACTOR = 2;
        final int DEFAULT_PQ_SIZE = 32;
        pq = new WaitablePQFixedSize<>(Math.max(DEFAULT_PQ_SIZE, numOfThreads * GROWTH_FACTOR));
        for (int i = 0; i < numOfThreads; i++) {
            Thread thread = new WorkingThread();
            thread.start();
        }

        isShutDown = false;
        PoisonAppleMax = createPoisonApple();
        PoisonAppleShutDown = createPoisonPill();
    }

    private Task<Void> createPoisonApple() {
        return new Task<>(() -> {
            ((WorkingThread) (Thread.currentThread())).setRunningToFalse();
            return null;
        }, MAX_PRIORITY, pq);
    }

    private Task<Void> createPoisonPill() {
        final int MIN_PRIORITY = -1;
        return new Task<>(() -> {
            ((WorkingThread) (Thread.currentThread())).setRunningToFalse();
            endedThreadsSem.release();
            return null;
        }, MIN_PRIORITY, pq);
    }

    public ThreadPool() {
        this(Runtime.getRuntime().availableProcessors() + 1);
    }

    private <V> Future<V> mySubmit(Callable<V> command, int priority) {
        if (isShutDown) {
            throw new RejectedExecutionException();
        }

        Task<V> task = new Task<>(command, priority, pq);
        try {
            pq.enqueue(task);
        } catch (InterruptedException ignore) {
        }

        return task.getFuture();
    }

    public <V> Future<V> submit(Callable<V> command, Priority priority) {
        return mySubmit(command, priority.ordinal());
    }

    public <V> Future<V> submit(Runnable command, Priority priority, V outParam) {
        return submit(runnableToCallable(command, outParam), priority);
    }

    public <V> Future<V> submit(Callable<V> command) {
        return submit(command, Priority.DEFAULT);
    }

    //Executors.callable(command, null);
    public <V> Future<V> submit(Runnable command, Priority priority) {
        return submit(runnableToCallable(command, null), priority);
    }

    @Override
    public void execute(Runnable runnable) {
        submit(runnableToCallable(runnable, null));
    }

    public void resumeTP() {
        isStopped = false;
        threadsSemaphore.release(getNumOfThreads());
    }

    public void setNumOfThreads(int numOfThreads) {
        setNumOfThreadsStrategy(numOfThreads);
    }

    private void setNumOfThreadsStrategy(int numOfThreads) {
        int diff = numOfThreads - getNumOfThreads();
        if (diff == 0) {
            return;
        }
        if (0 < diff) {
            for (int i = 0; i < diff; i++) {
                Thread thread = new WorkingThread();
                if (isStopped) {
                    try {
                        pq.enqueue(new Task<>(() -> {
                            threadsSemaphore.acquire();
                            return null;
                        }, MAX_PRIORITY, pq));
                    } catch (InterruptedException ignore) {
                    }
                }
                thread.start();
                this.numOfThreads.set(numOfThreads);
            }
        } else {
            diff = -1 * diff;
            //  insertPoisonApple(diff, PoisonAppleMax);

            for (int i = 0; i < diff; i++) {
                try {
                    pq.enqueue(PoisonAppleMax);
                } catch (InterruptedException ignore) {
                }
                threadsSemaphore.release();

            }
        }
        this.numOfThreads.set(numOfThreads);
    }

    public int getNumOfThreads() {
        return numOfThreads.get();
    }

    public void pauseTP() {
        isStopped = true;
        insertTaskToThreads(getNumOfThreads(), taskThreadPause());
//        for (int i = 0; i < getNumOfThreads(); i++) {
//            try {
//                pq.enqueue(taskThreadPause());
//            } catch (InterruptedException ignore) {
//            }
//        }
    }

    private Task<Void> taskThreadPause() {
        return new Task<>(() -> {
            threadsSemaphore.acquire();
            return null;
        }, MAX_PRIORITY, pq);
    }

    public void shutDown() {
        if (isShutDown) {
            return;
        }

        insertTaskToThreads(numOfThreads.get() - 1, PoisonAppleShutDown);
        insertLastPoisonApple();
        isShutDown = true;
    }

    private void insertLastPoisonApple() {
        Task<Void> PoisonPillTask = createLastPoisonAppleTask();
        lastPoisonPill = PoisonPillTask.getFuture();

        try {
            pq.enqueue(PoisonPillTask);
        } catch (InterruptedException ignore) {
        }
    }

    private void insertTaskToThreads(int appleNum, Task<Void> task) {
        for (int i = 0; i < appleNum; ++i) {
            try {
                pq.enqueue(task);
            } catch (InterruptedException ignore) {
            }
        }
    }

    private Task<Void> createLastPoisonAppleTask() {
        int MAXMINPRIORITY = -10;
        return new Task<>(() -> {
            ((WorkingThread) (Thread.currentThread())).setRunningToFalse();
            endedThreadsSem.acquire(getNumOfThreads() - 1);
            return null;
        }, MAXMINPRIORITY, pq);
    }

    //Blocks until all tasks have completed execution after a shutdown request,
    // or the timeout occurs, or the current thread is interrupted, whichever
    // happens first.
    public void awaitTermination() throws InterruptedException {
        awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    public boolean awaitTermination(long timeOut, TimeUnit unit) throws InterruptedException {
        try {
            lastPoisonPill.get(timeOut, unit);
        } catch (ExecutionException ignore) {
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    private static class Task<V> implements Comparable<Task<?>> {
        private final TFuture future;
        private final int priority;
        private final Callable<V> method;
        private Exception exception = null;
        Lock lock = new ReentrantLock();
        Condition cond = lock.newCondition();

        private Task(Callable<V> method, int priority, WaitablePQFixedSize<Task<?>> pq) {
            this.priority = priority;
            future = new TFuture(pq);
            this.method = method;
        }

        private Future<V> getFuture() {
            return future;
        }

        private int getPriority() {
            return priority;
        }

        private void run() {
            try {
                future.setValue(method.call());
                future.setDone(true);
                lock.lock();
                try {
                    cond.signal();
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                exception = e;
            }
        }

        private class TFuture implements Future<V> {
            private final WaitablePQFixedSize<Task<?>> pq;
            private volatile boolean isDone = false;
            private boolean isCancel = false;
            private V value = null;

            private TFuture(WaitablePQFixedSize<Task<?>> pq) {
                this.pq = pq;
            }

            public void setValue(V value) {
                this.value = value;
            }

            public V getValue() {
                return value;
            }

            public void setDone(boolean done) {
                isDone = done;
            }

            public void setCancel(boolean cancel) {
                isCancel = cancel;
            }

            @Override
            public boolean cancel(boolean b) {
                if (isDone) {
                    return false;
                }
                boolean res = pq.remove(Task.this);
                if (res) {
                    setCancel(true);
                }
                return res;
            }

            @Override
            public boolean isCancelled() {
                return isCancel;
            }

            @Override
            public boolean isDone() {
                return isDone;
            }

            @Override
            public V get() throws InterruptedException, ExecutionException {
                try {
                    return get(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                if (isCancel) {
                    throw new CancellationException();
                }
                long timeOut = timeUnit.toNanos(l);
                lock.lock();
                try {
                    while (!isDone() && timeOut > 0) {
                        timeOut = cond.awaitNanos(timeOut);
                    }
                } finally {
                    lock.unlock();
                }

                if (timeOut <= 0) {
                    throw new TimeoutException();
                }

                if (exception != null) {
                    throw new ExecutionException(exception);
                }

                return getValue();
            }
        }

        @Override
        public int compareTo(@NotNull Task<?> task) {
            return task.getPriority() - getPriority();
        }
    }

    private class WorkingThread extends Thread {
        private boolean isPushed;

        private WorkingThread() {
            isPushed = true;
        }

        @Override
        public void run() {
            while (isPushed) {
                try {
                    Task<?> task = pq.dequeue();
                    task.run();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        private void setRunningToFalse() {
            isPushed = false;
        }
    }

    private static <V> Callable<V> runnableToCallable(Runnable runnable, V result) {
        return () -> {
            runnable.run();
            return result;
        };
    }
}



