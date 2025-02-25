package connection.generichttpserver;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import connection.connectionserviceutils.IConnection;
import connection.connectionserviceutils.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static rps.ByteArrToJson.convertByteArrToJson;

public class HTTPIConnection implements IConnection {
    private final HttpExchange exchange;

    public HTTPIConnection(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public JsonObject read() throws IOException {
        InputStream input = exchange.getRequestBody();
        int inputLen = input.available();

        byte[] request = new byte[inputLen];

        int bytesRead = input.read(request, 0, inputLen);

        if (bytesRead != inputLen) {
            throw new IOException("OH NO!");
        }

        return convertByteArrToJson(request);

    }

    @Override
    public void write(String message, Status status) throws IOException {
        exchange.sendResponseHeaders(status.getStatusCode(), message.length());
        OutputStream out = exchange.getResponseBody();
        out.write(message.getBytes());
        out.close();
    }
}