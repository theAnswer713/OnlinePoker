import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class ClientListenThread implements Runnable {
    private Socket socket;
    private BufferedReader br;
    private List<Player> players;

    public ClientListenThread(Socket socket) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }

    public void run() {
        try {
            while(!socket.isClosed()) {
                String message = br.readLine();
                System.out.println(message);
                if(message.equals("start")) {
                    //put stuff to create playingScreen
                }

                // once playingScreen is created
                int playerNumber = Integer.parseInt(message.substring(message.length()-1));
                if(message.startsWith("fold")) {
                    players.get(playerNumber).fold();
                }
                if(message.startsWith("check")) {
                    players.get(playerNumber).check(Integer.parseInt(message.substring(6))); //maybe create method for this
                }
                if(message.startsWith("raise")) {
                    players.get(playerNumber).raise(Integer.parseInt(message.substring(6))); //maybe create method for this
                }
            }
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }
}
