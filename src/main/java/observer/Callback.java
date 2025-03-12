package observer;

import java.util.function.Consumer;

public class Callback<T> {
    private Dispatcher<T> dis;
    private final Consumer<T> func;
    private final Runnable stopService;

    public Callback(Consumer<T> func, Runnable stopService){
        this.func = func;
        this.stopService = stopService;
    }

    public void update(T data){
        func.accept(data);
    }

    public void unsubscribe(){
        dis.unsubscribe(this);
    }

    public void setDispatcher(Dispatcher<T> dis){
        this.dis = dis;
    }

    public void stopService(){
        // stop the service from the publisher side
        stopService.run();
    }
}