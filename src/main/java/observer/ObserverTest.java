package observer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {

    @Test
    public void testSingleSubscriber() {
        Publisher publisher = new Publisher();

        Subscriber subscriber = new Subscriber();

        subscriber.register(publisher);
        publisher.notifyAllSub("Hello");

        assertEquals("Hello", subscriber.getResult().toString());
    }

    @Test
    public void testMultipleSubscribers() {
        Publisher publisher = new Publisher();

        Subscriber subscriber1 = new Subscriber();

        Subscriber subscriber2 = new Subscriber();

        subscriber1.register(publisher);
        subscriber2.register(publisher);

        publisher.notifyAllSub("Hello");

        assertEquals("Hello", subscriber1.getResult().toString());
        assertEquals("Hello", subscriber2.getResult().toString());
    }

    @Test
    public void testUnsubscribe() {
        Publisher publisher = new Publisher();
        StringBuilder result = new StringBuilder();

        Subscriber subscriber = new Subscriber();

        subscriber.register(publisher);
        subscriber.unregister();

        publisher.notifyAllSub("Hello");

        assertEquals("", result.toString());
    }

    @Test
    public void testStopService() {
        Publisher publisher = new Publisher();
        StringBuilder result1 = new StringBuilder();
        StringBuilder result2 = new StringBuilder();

        Subscriber subscriber1 = new Subscriber();

        Subscriber subscriber2 = new Subscriber();

        subscriber1.register(publisher);
        subscriber2.register(publisher);

        publisher.stopService();

        assertTrue(subscriber1.getResult().toString().contains("Stopped"));
        assertTrue(subscriber2.getResult().toString().contains("Stopped"));
    }

    @Test
    public void testNoSubscribers() {
        Publisher publisher = new Publisher();

        assertDoesNotThrow(() -> publisher.notifyAllSub("Hello"));
    }
}
