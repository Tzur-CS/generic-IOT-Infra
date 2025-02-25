package connection.connectionservice;

import com.google.gson.JsonObject;
import connection.connectionserviceutils.IConnection;
import connection.connectionserviceutils.Status;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static rps.ByteArrToJson.convertByteArrToJson;

public class UDPIConnection implements IConnection {
    private static final int BUFFER_SIZE = 1024;
    private final DatagramChannel channel;
    private InetSocketAddress clientAddress = null;
    private final ByteBuffer buffer;

    public UDPIConnection(DatagramChannel channel) {
        this.channel = channel;
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    @Override
    public JsonObject read() throws IOException {
        buffer.clear();
        clientAddress = (InetSocketAddress) channel.receive(buffer);

        buffer.flip();
        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);

        return convertByteArrToJson(arr);
    }

    @Override
    public void write(String message, Status status) throws IOException {
        buffer.clear();
        String str = status.name() + " " + message;
        buffer.put(str.getBytes(), 0, str.length());

        buffer.flip();
        channel.send(buffer, clientAddress);
        buffer.clear();
    }
}
