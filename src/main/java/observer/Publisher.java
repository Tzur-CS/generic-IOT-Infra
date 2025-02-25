package observer;

public class Publisher {
    private final Dispatcher<String> dispatcher;

    public Publisher(){
        dispatcher = new Dispatcher<>();
    }

    public void registerCallback(Callback<String> call) {
        dispatcher.subscribe(call);
    }

    public void unregisterCallback(Callback<String> call) {
        dispatcher.unsubscribe(call);
    }

    public void notifyAllSub(String data){
        dispatcher.publish(data);
    }

    public void stopService(){
        dispatcher.stopService();
    }
}