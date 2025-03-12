package connection.generichttpserver;

import connection.connectionserviceutils.IConnection;

public interface Controller {
    void handle(IConnection connection);
}

