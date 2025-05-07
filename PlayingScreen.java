import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

//make debugging boolean that enables a bunch of prints when true
//if(debugging) System.out.println(sending, receiving, etc)

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import javax.sound.sampled.Clip;

public class PlayingScreen implements ActionListener {
    private Socket socket;
    private String name;
    private BufferedReader br;
    private PrintWriter pw;
    private ArrayList<Player> players;
    private int turnNumber;
    private Color green, white, brown;
    private JButton foldButton, checkButton, raiseButton;
    private JTextField amountField;
    private Clip clip;

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

        Thread listenThread = new Thread(new ListenThread());
        listenThread.start();

        JFrame frame = new JFrame("Poker");
        white = new Color(199, 199, 204);
        green = new Color(25, 70, 26);
        brown = new Color(50, 37, 12);
        //have server send playerList
        String playerList = br.readLine();
        String[] playerNames = playerList.split("/");
        players = new ArrayList<Player>();

        Player p1 = new Player(playerNames[0]);
        p1.setHole(br.readLine());
        players.add(p1);

        Player p2 = new Player(playerNames[1]);
        p2.setHole(br.readLine());
        players.add(p2);

        Player p3 = new Player(playerNames[2]);
        p3.setHole(br.readLine());
        players.add(p3);

        Player p4 = new Player(playerNames[3]);
        p4.setHole(br.readLine());
        players.add(p4);

        for(int i=0; i<playerNames.length; i++) {
            if(playerNames[i].equals(name)) {
                turnNumber = i;
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
        centerPanel.add(dealer);

        c.gridheight = 4;
        c.gridy = 1;
        JPanel table = Table(new ArrayList<Card>());
        //this will need to have cards from hand - cards in hole
        centerPanel.add(table); //send it the cards on the table

        c.gridheight = 1;
        c.gridy = 5;
        JPanel handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout());
        for(Card x:players.get(turnNumber).getHole()) {
            JLabel image = new JLabel();
            int value = x.getValue();
            if(value == 14) {
                value = 1;
            }
            String path = System.getProperty("user.dir")+"\\"+x.getSuit()+value+".png";
            ImageIcon icon = new ImageIcon(path);
            image.setIcon(icon);
            handPanel.add(image);
        }
        centerPanel.add(handPanel);

        //update all of these panels

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        foldButton = new JButton("FOLD");
        foldButton.addActionListener(this);
        buttons.add(foldButton);

        checkButton = new JButton("CHECK");
        checkButton.addActionListener(this);
        buttons.add(checkButton);

        raiseButton = new JButton("RAISE");
        raiseButton.addActionListener(this);
        buttons.add(raiseButton);

        amountField = new JTextField(1);
        buttons.add(amountField);

        //create buttons that appear under certain conditions
        //make buttons have functions that send certain messages
        //code the messages to start with unique identifiers
        //determine what those identifiers will be

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playSound();
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
                    System.out.println(message);
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
                    //add for other buttons
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        try{
            if(e.getSource() == foldButton) {
                players.get(turnNumber).fold();
                pw.println("fold");
            }
            if(e.getSource() == checkButton) {
                pw.println("check");
            }
            if(e.getSource() == raiseButton) {
                pw.println("raise"+amountField.getText());
            }
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds\\Noir Noises.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public JPanel Table(ArrayList<Card> tableCards) {
        JPanel table = new JPanel();
        table.setLayout(new FlowLayout());
        table.setBackground(brown);

        for(Card x:tableCards) {
            JLabel image = new JLabel();
            int value = x.getValue();
            if(value == 14) {
                value = 1;
            }
            String path = System.getProperty("user.dir")+"\\"+x.getSuit()+value+".png";
            ImageIcon icon = new ImageIcon(path);
            image.setIcon(icon);
            table.add(image);
        }

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
