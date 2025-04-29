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
        //have server send playerList

        //where the game is shown and players are
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //JPanel info1 = PlayerInfo(p1, "l");

        //JPanel info2 = PlayerInfo(p2, "r");

        //JPanel info3 = PlayerInfo(p3, "r");

        //JPanel info4 = PlayerInfo(p4, "l");


        JLabel n1 = new JLabel("");
        JLabel n2 = new JLabel("");
        JLabel n3 = new JLabel("");
        JLabel n4 = new JLabel("");

        //set layout of different items in grid
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;

        //find way to assign the four players to items where we can call methods on each

        c.gridheight = 2;

        //centerPanel.add(panel1, c);
        //create class to update all of these panels


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