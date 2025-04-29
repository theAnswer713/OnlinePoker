import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.List;
import java.util.ArrayList;

public class Server {
    private ServerSocket server;
    private List<Player> playerList; //change to list

    public static void main(String[] args) throws Exception {
        new Server();
    }

    public Server() throws Exception {
        this.server = new ServerSocket(55555);
        this.playerList = new ArrayList<Player>();

        Thread acceptThread = new Thread(new AcceptThread());
        acceptThread.start();
        acceptThread.join();
    }

    private class AcceptThread implements Runnable {
        public void run() {
            String nameList = "";
            try {
                while(!server.isClosed()) {
                    while(playerList.size()<4) {
                        System.out.println("Waiting for players to join...");
                        Socket socket = server.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String name = br.readLine();
                        nameList += name+"/";
                        Player player = new Player(name, socket);
                        playerList.add(player);

                        Thread listenThread = new Thread(new ListenThread(player));
                        listenThread.start();
                    }
                    System.out.println("All players have joined!");
                    nameList = nameList.substring(0, nameList.length()-1);
                    for(Player x:playerList) {
                        x.getPw().println("start");
                        x.getPw().println(nameList);
                    }
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
                    for(Player x: playerList) {
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
