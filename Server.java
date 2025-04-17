import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Set;
import java.util.LinkedHashSet;

public class Server {
    private ServerSocket server;
    private Set<Player> playerSet;

    public static void main(String[] args) throws Exception {
        new Server();
    }

    public Server() throws Exception {
        this.server = new ServerSocket(55555);
        this.playerSet = new LinkedHashSet<Player>();

        Thread acceptThread = new Thread(new AcceptThread());
        acceptThread.start();
        acceptThread.join();
    }

    private class AcceptThread implements Runnable {
        public void run() {
            try {
                while(!server.isClosed()) {
                    while(playerSet.size()<4) {
                        System.out.println("Waiting for players to join...");
                        Socket socket = server.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String name = br.readLine();
                        Player player = new Player(name, socket);
                        playerSet.add(player);

                        Thread listenThread = new Thread(new ListenThread(player));
                        listenThread.start();

                        String message = "JOIN:"+name;
                        System.out.println(message);
                        for(Player x:playerSet) {
                            x.getPw().println(message);
                        }
                    }
                    System.out.println("All players have joined!");
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    private class ListenThread implements Runnable {
        private Player player;

        public ListenThread(Player player) {
            this.player = player;
        }

        public void run() {
            try {
                while(!player.getSocket().isClosed()) {
                    String message = player.getBr().readLine();
                    System.out.println(message);
                    for(Player x:playerSet) {
                        x.getPw().println(message);
                    }
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }
}
