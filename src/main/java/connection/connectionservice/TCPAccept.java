package connection.connectionservice;

import connection.connectionserviceutils.EventHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

class TCPAccept implements Handler {
    private final ServerSocketChannel channel;
    private final Selector selector;
    private final EventHandler handler;

    public TCPAccept(Selector selector, ServerSocketChannel channel, EventHandler handler) {
        this.channel = channel;
        this.selector = selector;
        this.handler = handler;
    }

    @Override
    public void handle() throws IOException {
        if (!handler.onAccept(null)) {
            return;
        }

        SocketChannel client = channel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, new TCPRead(client, handler));
    }
}
