import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
    private BufferedReader br;
    private PrintWriter pw;

    public PlayingScreen(Socket socket) throws Exception {
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
        String playerList = br.readLine();
        String[] playerNames = playerList.split("/");
        Player p1 = new Player(playerNames[0]);
        Player p2 = new Player(playerNames[1]);
        Player p3 = new Player(playerNames[2]);
        Player p4 = new Player(playerNames[3]);


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

    //in actionperformed (i think)
    //have buttons send specific message through printwriter to server
    //so for instance, pushing check sends "check", raise sends # amount
    //fold sends "fold"

    private class ListenThread implements Runnable {
        public void run() {
            try{
                while(!socket.isClosed()) {
                    String message = br.readLine();

                    //add else ifs with different beginnings of a message
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("GameSound.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
