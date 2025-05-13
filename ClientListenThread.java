import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientListenThread implements Runnable {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private String name;
    private MainMenu mainMenu;
    private List<Player> players;

    public ClientListenThread(Socket socket, String name, MainMenu mainMenu) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
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
                    //stuff to create playingScreen
                    //these two below might need to be private with getter methods
                    mainMenu.frame.dispose();
                    mainMenu.clip.stop();

                    System.out.println("All players have joined!");
                    new PlayingScreen(socket, name);
                    System.out.println("Playing screen created");
                }

                // once playingScreen is created
                while(!socket.isClosed()) {
                    message = br.readLine();
                    System.out.println(message);
                    int playerNumber = Integer.parseInt(message.substring(message.length() - 1));
                    message = message.substring(0, message.length()-1);

                    //need to finish this line with "folded", "checked", or "raised __ (insert amount)"
                    System.out.println("Turn "+playerNumber+": "+players.get(playerNumber).getName()+" has ");
                    if (message.startsWith("fold")) {
                        //PlayingScreen.players.get(playerNumber).fold(); do this for the other two but find how to call it
                        players.get(playerNumber).fold();
                    }
                    if (message.startsWith("check")) {
                        players.get(playerNumber).check(Integer.parseInt(message.substring(6)));
                    }
                    if (message.startsWith("raise")) {
                        players.get(playerNumber).raise(Integer.parseInt(message.substring(6)));
                    }
                }
            }
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }
}
