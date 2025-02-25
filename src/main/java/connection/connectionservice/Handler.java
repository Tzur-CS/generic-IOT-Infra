package connection.connectionservice;

import java.io.IOException;

interface Handler {
    void handle() throws IOException;
}
