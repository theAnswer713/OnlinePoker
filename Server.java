import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Set;
import java.util.LinkedHashSet;

public class Server {
    private ServerSocket server;
    private Set<PlayerData> playerSet;

    public static void main(String[] args) throws Exception {
        new Server();
    }

    public Server() throws Exception {
        this.server = new ServerSocket(55555);
        this.playerSet = new LinkedHashSet<PlayerData>();

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
                        String username = br.readLine();
                        PlayerData pd = new PlayerData(socket,username);
                        playerSet.add(pd);

                        Thread listenThread = new Thread(new ListenThread(pd));
                        listenThread.start();

                        String message = "JOIN:"+username;
                        System.out.println(message);
                        for(PlayerData x:playerSet) {
                            x.getPw().println(message);
                        }
                    }
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    private class ListenThread implements Runnable {
        private PlayerData pd;

        public ListenThread(PlayerData pd) {
            this.pd = pd;
        }

        public void run() {
            try {
                while(!pd.getSocket().isClosed()) {
                    String message = pd.getBr().readLine();
                }
            }
            catch(Exception err) {

            }
        }
    }
}
