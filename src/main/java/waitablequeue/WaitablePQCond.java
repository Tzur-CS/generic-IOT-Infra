package waitablequeue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitablePQCond<E> {
    private final Queue<E> priorityQueue;
    Lock lock = new ReentrantLock();
    Condition cond = lock.newCondition();

    public WaitablePQCond(){
        priorityQueue = new PriorityQueue<>();
    }

    public WaitablePQCond(Comparator<? super E> cmp){
        priorityQueue = new PriorityQueue<>(cmp);
    }

    public void enqueue(E element){
        lock.lock();
        priorityQueue.add(element);
        cond.signal();
        lock.unlock();
    }

    public E dequeue() throws InterruptedException{
        lock.lock();
        try {
            while (priorityQueue.isEmpty()) {
                cond.await();
            }
            return priorityQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(Object o){
        boolean retVal;
        lock.lock();
        retVal = priorityQueue.remove(o);
        lock.unlock();
        return retVal;

    }

}
