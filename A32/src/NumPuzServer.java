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
    NumPuzServerGui gui;
    String clientName;
    HashMap<Integer, ArrayList<Integer>> playerStats = new HashMap<>();
    HashMap<Integer, String> playerNames = new HashMap<>();
    int nextClientId = 1;
    HashMap<Integer, Socket> clientSockets = new HashMap<>();

    public NumPuzServer(int port, NumPuzServerGui gui) throws IOException {
        socket = new ServerSocket(port);
        newClientThread = new Thread(new AcceptClient());
        this.gui = gui;
        // Start listening for new clients forever
        newClientThread.start();
    }

    private class AcceptClient implements Runnable {

        @Override
        public void run() {
            // Always accept new clients (as long as server is up)
            while (true) {
                Thread listenForMessageThread;
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
                    playerNames.put(nextClientId, clientName);
                    // Keep track of every client's socket so we can access them later
                    clientSockets.put(nextClientId, client);
                    // Send the ID to the client
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println("id:" + nextClientId);
                    gui.getLogTextArea().append("Sent client ID to " + clientName + "\n");
                    // Create a new thread so that each client can listen simulatenously
                    listenForMessageThread = new Thread(new ListenForMessage(nextClientId));
                    // Make sure every user has a unique ID
                    nextClientId++;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                listenForMessageThread.start();
            }
        }
    }

    /**
     * Allows each client to listen to the server simultaneously
     */
    private class ListenForMessage implements Runnable {
        private int clientId;

        ListenForMessage(int clientId) {
            this.clientId = clientId;
        }
        @Override
        public void run() {
            try {
                Socket clientSocket = clientSockets.get(this.clientId);
                System.out.println("Started listening for a message");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Still listening");
                    if (message.startsWith("config:")) {
                        String config = message.split(":")[1];
                        gui.setConfiguration(config);
                        gui.getLogTextArea().append("Received configuration: " + config + "\n");
                    } else if (message.startsWith("requestConfig")) {
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println("config:" + gui.getConfiguration());
                        gui.getLogTextArea().append("Sent current config to " + playerNames.get(this.clientId) + "\n");
                    } else if (message.startsWith("data:")) {
                        String data = message.split(":")[1];
                        String[] dataSplit = data.split(",");
                        int clientId = Integer.parseInt(dataSplit[0]);
                        int moves = Integer.parseInt(dataSplit[1]);
                        int score = Integer.parseInt(dataSplit[2]);
                        int time = Integer.parseInt(dataSplit[3]);

                        playerStats.get(clientId).set(0, moves);
                        playerStats.get(clientId).set(1, score);
                        playerStats.get(clientId).set(2, time);
                        String output =
                        "Got data from client " + playerNames.get(clientId) + ": " + moves + " moves, "
                                + score + " score, " + time + " time\n";
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

    public HashMap<Integer, ArrayList<Integer>> getPlayerStats() {
        return playerStats;
    }

    public HashMap<Integer, String> getPlayerNames() {
        return playerNames;
    }
}
