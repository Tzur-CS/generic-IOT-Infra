package gatewayserver;

import com.google.gson.JsonObject;
import connection.connectionservice.ConnectionService;
import connection.connectionserviceutils.EventHandler;
import connection.connectionserviceutils.IConnection;
import connection.connectionserviceutils.Status;
import connection.generichttpserver.Controller;
import connection.generichttpserver.GenericHTTPServer;
import rps.RPS;

import java.io.IOException;

public class GatewayServer {
    private final RPS rps = new RPS();
    private ConnectionService connectionService;
    private final EventHandlerPolice eventHandlerPolice = new EventHandlerPolice();
    private GenericHTTPServer httpServer;

    public void startServer(String address, int TCPPort, int UDPPort) {
        try {
            connectionService = new ConnectionService(eventHandlerPolice);
            connectionService.addTCPConnection(address, TCPPort);
            connectionService.addUDPConnection(address, UDPPort);
            connectionService.start();
        } catch (IOException e) {
            connectionService = null;
            throw new RuntimeException(e);
        }
    }

    public void stopServer(){
        connectionService.stop();
    }


    public void startHttpServer() {
        try {
            httpServer = new GenericHTTPServer();
            httpServer.addRoute("/iots", new HTTPController());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopHttpServer() {
        httpServer.stop();
    }



    private class EventHandlerPolice implements EventHandler {

        @Override
        public boolean onAccept(IConnection connection) {
            return true;
        }

        @Override
        public void onRead(IConnection connection) {
            JsonObject json;
            try {
                json = connection.read();
                if (json == null) {
                    return;
                }
            } catch (IOException e) {
                return;
            }

            Message message = new Message(this, connection, json);
            rps.handleRequest(message);
        }

        @Override
        public void onWrite(IConnection connection, String message, Status status) {
            try {
                connection.write(message, status);
            } catch (IOException e) {
                return;
            }
        }
    }

    private class HTTPController implements Controller {

        @Override
        public void handle(IConnection connection) {
            eventHandlerPolice.onRead(connection);
        }
    }
}
