package waitablequeue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class WaitablePQFixedSize<E> {
    private final Queue<E> priorityQueue;
    private final Semaphore semaphoreWrite;
    private final Semaphore semaphoreRead = new Semaphore(0);

    public WaitablePQFixedSize(int size){
        priorityQueue = new PriorityQueue<>();
        semaphoreWrite = new Semaphore(size);
    }

    public WaitablePQFixedSize(int size, Comparator<? super E> cmp){
        priorityQueue = new PriorityQueue<>(cmp);
        semaphoreWrite = new Semaphore(size);
    }

    public void enqueue(E element) throws InterruptedException {
        semaphoreWrite.acquire();
        synchronized (priorityQueue) {
            priorityQueue.add(element);
        }
        semaphoreRead.release();
    }

    public E dequeue() throws InterruptedException{
        semaphoreRead.acquire();

        synchronized (priorityQueue){
            semaphoreWrite.release();
            return priorityQueue.poll();
        }
    }

    public boolean remove(Object o){
        boolean removed = false;
        boolean semIsAcquired = semaphoreRead.tryAcquire();

        if (semIsAcquired) {
            synchronized (priorityQueue) {
                removed = priorityQueue.remove(o);
            }
        }

        if (removed) {
            semaphoreWrite.release();
        }
        else if (semIsAcquired) {
            semaphoreRead.release();
        }

        return removed;
    }


}
