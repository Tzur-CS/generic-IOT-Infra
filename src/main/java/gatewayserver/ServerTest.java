package gatewayserver;

public class ServerTest {
    public static void main(String[] args) {
        new GatewayServer().startServer("localhost", 12345, 12346);
    }
}
