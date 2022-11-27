import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class NumPuzServer {
    ServerSocket socket;
    Socket client;
    Thread newClientThread;
    NumPuzServerGui gui;

    public NumPuzServer(int port, NumPuzServerGui gui) throws IOException {
        socket = new ServerSocket(port);
        newClientThread = new Thread(new AcceptClient());
        this.gui = gui;
        newClientThread.start();
    }

    private class AcceptClient implements Runnable {

        @Override
        public void run() {
            try {
                client = socket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                gui.getLogTextArea().append("New connected user: " + in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Socket getClient() {
        if (client != null) {
            return client;
        }
        return null;
    }
}
