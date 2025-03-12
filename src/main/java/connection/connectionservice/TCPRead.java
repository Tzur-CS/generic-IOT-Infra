package connection.connectionservice;

import connection.connectionserviceutils.EventHandler;
import connection.connectionserviceutils.IConnection;

import java.io.IOException;
import java.nio.channels.SocketChannel;

class TCPRead implements Handler {
    private final EventHandler handler;
    private final IConnection iConnection;

    public TCPRead(SocketChannel channel, EventHandler handler) {
        this.handler = handler;
        iConnection = new TCPIConnection(channel);
    }

    @Override
    public void handle() throws IOException {
        handler.onRead(iConnection);
    }

}
