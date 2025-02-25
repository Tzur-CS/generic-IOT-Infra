package connection.connectionserviceutils;

import java.io.IOException;

public interface EventHandler {
    boolean onAccept(IConnection connection);
    void onRead(IConnection connection) throws IOException;
    void onWrite(IConnection connection, String message, Status status) throws IOException;
}