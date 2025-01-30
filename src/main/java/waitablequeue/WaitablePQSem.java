package waitablequeue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class WaitablePQSem<E> {
    private final Queue<E> priorityQueue;
    private final Semaphore semaphore = new Semaphore(0);

    public WaitablePQSem(){
        priorityQueue = new PriorityQueue<>();
    }

    public WaitablePQSem(Comparator<? super E> cmp){
        priorityQueue = new PriorityQueue<>(cmp);
    }

    public void enqueue(E element){
        synchronized (priorityQueue) {
            priorityQueue.add(element);
        }
        semaphore.release();
    }

    public E dequeue() throws InterruptedException{
        semaphore.acquire();

        synchronized (priorityQueue){
            return priorityQueue.poll();
        }
    }

    public boolean remove(Object o){
        boolean result = false;
        boolean semIsAcquired = semaphore.tryAcquire();

        if (semIsAcquired) {
            synchronized (priorityQueue) {
                result = priorityQueue.remove(o);
            }
        }

        if (semIsAcquired && !result) {
            semaphore.release();
        }

        return result;
    }

}
