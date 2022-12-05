import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class NumPuzServer {
    ServerSocket socket;
    Socket client;
    Thread newClientThread;
    Thread listenForMessageThread;
    NumPuzServerGui gui;
    String clientName;
    HashMap<Integer, ArrayList<Integer>> playerStats = new HashMap<>();
    int nextClientId = 1;

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
                clientName = in.readLine();
                gui.getLogTextArea().append("New connected user: " + clientName + ", ID: " + nextClientId + "\n");
                // Moves, score, time
                ArrayList<Integer> newStats = new ArrayList<>();
                newStats.add(0);
                newStats.add(0);
                newStats.add(0);
                playerStats.put(nextClientId, newStats);
                // Send the ID to the client
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println("id:" + nextClientId);
                gui.getLogTextArea().append("Sent client ID to " + clientName + "\n");
                nextClientId++;
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
                    } else if (message.startsWith("requestConfig")) {
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        out.println("config:" + gui.getConfiguration());
                        gui.getLogTextArea().append("Sent current config to " + clientName + "\n");
                    } else if (message.startsWith("data:")) {
                        String data = message.split(":")[1];
                        String[] dataSplit = data.split(",");
                        String moves = dataSplit[0];
                        String score = dataSplit[1];
                        String time = dataSplit[2];
                        String output =
                        "Got data from client " + clientName + ": " + moves + " moves, " + score + " score\n";
                        gui.getLogTextArea().append(output);
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
