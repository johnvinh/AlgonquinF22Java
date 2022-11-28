import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class NumPuzServer {
    ServerSocket socket;
    Socket client;
    Thread newClientThread;
    Thread listenForMessageThread;
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
                gui.getLogTextArea().append("New connected user: " + in.readLine() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            listenForMessageThread = new Thread(new ListenForMessage());
            listenForMessageThread.start();
        }
    }

    private class ListenForMessage implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Started listening for a message");
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Still listening");
                    if (message.startsWith("config:")) {
                        String config = message.split(":")[1];
                        gui.setConfiguration(config);
                        gui.getLogTextArea().append("Received configuration: " + config + "\n");
                    }
                }
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
