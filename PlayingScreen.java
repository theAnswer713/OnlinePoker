import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayingScreen {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public PlayingScreen(Socket socket) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(Exception err) {
            err.printStackTrace();
        }

        JFrame frame = new JFrame("Poker");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        //where the game is shown and players are

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        //where the action buttons are and player's cards

        //create buttons that appear under certain conditions
        //make buttons have functions that send certain messages
        //code the messages to start with unique identifiers
        //determine what those identifiers will be

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private class ListenThread implements Runnable {
        public void run() {
            try{
                while(!socket.isClosed()) {
                    String message = br.readLine();
                    if(message.startsWith("JOIN")) {
                        //add players in
                    }
                    //add else ifs with different beginnings of a message
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }
}