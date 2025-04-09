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

                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    private class ListenThread implements Runnable {
        public void run() {
            try {

            }
            catch(Exception err) {

            }
        }
    }
}
