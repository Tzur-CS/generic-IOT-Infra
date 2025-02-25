package connection.connectionservice;

import connection.connectionserviceutils.EventHandler;
import connection.connectionserviceutils.IConnection;

import java.io.IOException;
import java.nio.channels.DatagramChannel;


class UDPRead implements Handler {
    private final EventHandler handler;
    private final IConnection iConnection;

    public UDPRead(DatagramChannel channel, EventHandler handler){
        this.handler = handler;
        iConnection = new UDPIConnection(channel);
    }

    @Override
    public void handle() throws IOException {
        handler.onRead(iConnection);
    }
}
