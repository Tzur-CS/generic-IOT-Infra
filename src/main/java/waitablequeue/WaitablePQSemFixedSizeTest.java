package waitablequeue;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class WaitablePQSemFixedSizeTest {
    @Test
    public void testEnqueueAndDequeue() throws InterruptedException {
        WaitablePQFixedSize<Integer> pq = new WaitablePQFixedSize<>(3);

       pq.enqueue(10);
       pq.enqueue(5);
       pq.enqueue(20);

        assertEquals(5, pq.dequeue());
        assertEquals(10, pq.dequeue());
        assertEquals(20, pq.dequeue());
    }

    @Test
    public void testDequeueBlocksUntilEnqueue() throws InterruptedException {
        WaitablePQFixedSize<Integer> pq = new WaitablePQFixedSize<>(3);
        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(1000); // Delay enqueue to simulate blocking
                pq.enqueue(42);
            } catch (InterruptedException ignored) {
            }
        });

        producer.start();

        long startTime = System.currentTimeMillis();
        int result = pq.dequeue(); // This will block until producer enqueues
        long endTime = System.currentTimeMillis();

        assertEquals(42, result);
        assertTrue((endTime - startTime) >= 1000, "Dequeue should block until an element is enqueued");
    }

    @Test
    public void testRemove() throws InterruptedException {
        WaitablePQFixedSize<Integer> pq = new WaitablePQFixedSize<>(3);

        pq.enqueue(1);
        pq.enqueue(2);
        pq.enqueue(3);

        assertTrue(pq.remove(2), "Element 2 should be removed");
        assertFalse(pq.remove(4), "Element 4 does not exist in the queue");

        try {
            assertEquals(1, pq.dequeue());
            assertEquals(3, pq.dequeue());
        } catch (InterruptedException e) {
            fail("Dequeue was interrupted unexpectedly");
        }
    }

    @Test
    public void testCustomComparator() throws InterruptedException {
        WaitablePQFixedSize<Integer> pq = new WaitablePQFixedSize<>(3,Comparator.reverseOrder());

        pq.enqueue(1);
        pq.enqueue(3);
        pq.enqueue(2);

        assertEquals(3, pq.dequeue());
        assertEquals(2, pq.dequeue());
        assertEquals(1, pq.dequeue());
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        WaitablePQFixedSize<Integer> pq = new WaitablePQFixedSize<>(3);
        int numberOfProducers = 5;
        int numberOfConsumers = 5;
        int itemsPerProducer = 20;

        // Atomic counters for tracking
        AtomicInteger producedItems = new AtomicInteger(0);
        AtomicInteger consumedItems = new AtomicInteger(0);

        // Producer threads
        Thread[] producers = new Thread[numberOfProducers];
        for (int i = 0; i < numberOfProducers; i++) {
            producers[i] = new Thread(() -> {
                for (int j = 0; j < itemsPerProducer; j++) {
                    try {
                        pq.enqueue(producedItems.incrementAndGet());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        // Consumer threads
        Thread[] consumers = new Thread[numberOfConsumers];
        for (int i = 0; i < numberOfConsumers; i++) {
            consumers[i] = new Thread(() -> {
                while (true) {
                    try {
                        pq.dequeue();
                        consumedItems.incrementAndGet();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
        }

        for (Thread producer : producers) {
            producer.start();
        }
        for (Thread consumer : consumers) {
            consumer.start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        Thread.sleep(1000);

        for (Thread consumer : consumers) {
            consumer.interrupt();
        }

        for (Thread consumer : consumers) {
            consumer.join();
        }

        assertEquals(producedItems.get(), consumedItems.get(), "All produced items should be consumed");
    }
}
