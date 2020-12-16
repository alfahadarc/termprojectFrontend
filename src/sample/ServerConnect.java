package sample;

import java.io.IOException;
import java.net.Socket;

public class ServerConnect {

    private static ServerConnect connector = null;
    private Socket socket;

    private ServerConnect() {
        try {
            this.socket = new Socket("localhost", 4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerConnect getInstance()
    {
        if(connector == null)
            connector = new ServerConnect();

        return connector;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
