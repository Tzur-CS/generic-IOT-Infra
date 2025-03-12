package connection.connectionservice;

import connection.connectionserviceutils.EventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ConnectionService {
    private static volatile boolean isRunning = true;
    private final Selector selector;
    private final EventHandler handler;

    public ConnectionService(EventHandler handler) throws IOException {
        selector = Selector.open();
        this.handler = handler;
    }

    public void start() {
        try {
            while (isRunning) {
                if (selector.select() == 0) {
                    continue;
                }

                for (SelectionKey key : selector.selectedKeys()) {
                    ((Handler) key.attachment()).handle();
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        isRunning = false;
    }

    public void addTCPConnection(String ipAddress, int port) throws IOException {
        ServerSocketChannel tcpServerChannel = ServerSocketChannel.open();
        (tcpServerChannel).bind(new InetSocketAddress(ipAddress, port));
        tcpServerChannel.configureBlocking(false);
        tcpServerChannel.register(selector, SelectionKey.OP_ACCEPT, new TCPAccept(selector, tcpServerChannel, handler));
        System.out.println("New TCP connection added");
    }

    public void addUDPConnection(String ipAddress, int port) throws IOException {
        DatagramChannel udpServerChannel = DatagramChannel.open();

        (udpServerChannel).bind(new InetSocketAddress(ipAddress, port));
        udpServerChannel.configureBlocking(false);
        udpServerChannel.register(selector, SelectionKey.OP_READ, new UDPRead(udpServerChannel, handler));
        System.out.println("New UDP connection added");
    }
}
