package observer;

import java.util.function.Consumer;

public class Subscriber {
    private final StringBuilder result = new StringBuilder();
    private final Runnable stopService = () -> result.append("Stopped");
    private Consumer<String> func = data -> result.append(data);
    private final Callback<String> callback = new Callback<>(func, stopService);

    public StringBuilder getResult() {
        return result;
    }

    public void unregister() {
        callback.unsubscribe();
    }

    public void register(Publisher publisher) {
        publisher.registerCallback(callback);
    }
}