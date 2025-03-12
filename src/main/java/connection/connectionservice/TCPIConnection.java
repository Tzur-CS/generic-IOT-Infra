package connection.connectionservice;

import com.google.gson.JsonObject;
import connection.connectionserviceutils.IConnection;
import connection.connectionserviceutils.Status;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static rps.ByteArrToJson.convertByteArrToJson;

public class TCPIConnection implements IConnection {
    private final SocketChannel channel;
    private static final int BUFFER_SIZE = 1024;
    private final ByteBuffer buffer;

    public TCPIConnection(SocketChannel channel) {
        this.channel = channel;
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    @Override
    public JsonObject read() throws IOException {
        buffer.clear();
        int bytesRead = channel.read(buffer);

        if (channelDisconnection(bytesRead, channel)) {
            return null;
        }

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
        channel.write(buffer);
        buffer.clear();
    }


    private boolean channelDisconnection(int bytesRead, SocketChannel clientChannel) throws IOException {
        if (bytesRead == -1) {
            System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
            clientChannel.close();
            return true;
        }
        return false;
    }
}
