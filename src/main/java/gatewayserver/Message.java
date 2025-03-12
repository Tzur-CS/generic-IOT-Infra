package gatewayserver;

import com.google.gson.JsonObject;
import connection.connectionserviceutils.EventHandler;
import connection.connectionserviceutils.IConnection;
import connection.connectionserviceutils.Status;

import java.io.IOException;

public class Message {
    private final EventHandler handler;
    private final IConnection connection;
    private final JsonObject request;

    public Message(EventHandler handler, IConnection connection, JsonObject request){
        this.handler = handler;
        this.connection = connection;
        this.request = request;
    }

    public JsonObject getRequest() {
        return request;
    }

    public void sendFeedback(String message, Status status) throws IOException {
        handler.onWrite(connection, message, status);
    }

}