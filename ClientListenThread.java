import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class ClientListenThread implements Runnable {
    private Socket socket;
    private BufferedReader br;
    private String name;
    private MainMenu mainMenu;
    private List<Player> players;

    public ClientListenThread(Socket socket, String name, MainMenu mainMenu) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = name;
            this.mainMenu = mainMenu;
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

                    //these two below might need to be private with getter methods
                    mainMenu.frame.dispose();
                    mainMenu.clip.stop();

                    System.out.println("All players have joined!");
                    new PlayingScreen(socket, name);
                    System.out.println("Playing screen created");
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
