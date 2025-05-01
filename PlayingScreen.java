import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

//make debugging boolean that enables a bunch of prints when true
//if(debugging) System.out.println(sending, receiving, etc)

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlayingScreen {
    private Socket socket;
    private String name;
    private BufferedReader br;
    private PrintWriter pw;
    private int turnNumber;
    private Color green, white, brown;

    public PlayingScreen(Socket socket, String name) throws Exception {
        try {
            this.socket = socket;
            this.name = name;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(Exception err) {
            err.printStackTrace();
        }

        JFrame frame = new JFrame("Poker");
        green = new Color(25, 70, 26);
        white = new Color(199, 199, 204);
        brown = new Color(50, 37, 12);
        //have server send playerList
        String playerList = br.readLine();
        String[] playerNames = playerList.split("/");
        Player p1 = new Player(playerNames[0]);
        Player p2 = new Player(playerNames[1]);
        Player p3 = new Player(playerNames[2]);
        Player p4 = new Player(playerNames[3]);
        for(int i=0; i<playerNames.length; i++) {
            if(playerNames[i].equals(name)) {
                turnNumber = i+1;
            }
        }

        //where the game is shown and players are
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //set layout of different items in grid
        c.gridwidth = 2;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        JPanel info1 = PlayerInfo(p1, "l");
        centerPanel.add(info1, c);

        c.gridx = 7;
        JPanel info2 = PlayerInfo(p2, "r");
        centerPanel.add(info2, c);

        c.gridy = 2;
        JPanel info3 = PlayerInfo(p3, "r");
        centerPanel.add(info3, c);

        c.gridx = 0;
        JPanel info4 = PlayerInfo(p4, "l");
        centerPanel.add(info4, c);

        c.gridheight = 1;
        c.gridwidth = 4;
        c.gridx = 2;
        c.gridy = 0;
        JPanel dealer = new JPanel();

        c.gridheight = 4;
        c.gridy = 1;
        JPanel table = Table();

        //update all of these panels

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

    //in actionperformed (i think)
    //have buttons send specific message through printwriter to server
    //so for instance, pushing check sends "check", raise sends # amount
    //fold sends "fold"

    private class ListenThread implements Runnable {
        public void run() {
            try{
                while(!socket.isClosed()) {

                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Noir Noises.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public JPanel Table() {
        //add ArrayList<Card> to constructor
        JPanel table = new JPanel();
        table.setLayout(new FlowLayout());
        table.setBackground(brown);
        return table;
    }

    public JPanel PlayerInfo(Player player, String side) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        if(side.equals("l")) {
            JLabel name = new JLabel(player.getName());
            c.gridx = 0;
            c.gridy = 0;
            panel.add(name, c);
            name.setForeground(white);

            JLabel money = new JLabel("$"+player.getMoney());
            c.gridy = 1;
            panel.add(money, c);
            money.setForeground(white);

            JLabel image = new JLabel();
            String path = System.getProperty("user.dir")+"\\person.png";
            ImageIcon icon = new ImageIcon(path);
            image.setIcon(icon);
            c.gridheight = 2;
            c.gridx = 0;
            c.gridy = 1;
            panel.add(image, c);
        }
        if(side.equals("r")) {
            JLabel name = new JLabel(player.getName());
            c.gridx = 1;
            c.gridy = 0;
            panel.add(name, c);
            name.setForeground(white);

            JLabel money = new JLabel("$"+player.getMoney());
            c.gridy = 1;
            panel.add(money, c);
            money.setForeground(white);

            JLabel image = new JLabel();
            String path = System.getProperty("user.dir")+"\\person.png";
            ImageIcon icon = new ImageIcon(path);
            image.setIcon(icon);
            c.gridheight = 2;
            c.gridx = 0;
            c.gridy = 0;
            panel.add(image, c);
        }
        panel.setBackground(green);
        return panel;
    }
}
