import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.File;

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

public class MainMenu implements ActionListener {
    public JFrame frame;
    private JTextField ipField, nameField;
    private JButton joinButton;
    public Clip clip;
    private BufferedReader br;
    private PrintWriter pw;
    private Socket socket;
    private String name;

    public MainMenu() {
        frame = new JFrame("Poker");
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.insets = new Insets(10, 200, 10, 200);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        JLabel topLabel = new JLabel("Poker");
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(topLabel, c);

        c.gridwidth = 1;
        c.gridy = 1;
        JLabel ipLabel = new JLabel("Enter IP:");
        panel.add(ipLabel, c);
        c.gridx = 1;
        ipField = new JTextField(1);
        panel.add(ipField, c);

        c.gridx = 0;
        c.gridy = 2;
        JLabel nameLabel = new JLabel("Name:");
        panel.add(nameLabel, c);
        c.gridx = 1;
        nameField = new JTextField(1);
        panel.add(nameField, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        this.joinButton = new JButton("JOIN");
        joinButton.addActionListener(this);
        panel.add(joinButton, c);

        Color green = new Color(25, 70, 26);
        panel.setBackground(green);
        joinButton.setBackground(green);

        Color white = new Color(199, 199, 204);
        topLabel.setForeground(white);
        joinButton.setForeground(white);
        ipLabel.setForeground(white);
        nameLabel.setForeground(white);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setSize(1200, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playSound();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println("Trying to connect...");
            socket = new Socket(ipField.getText(), 55542);
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected!");
            System.out.println("ListenThread started!");
            System.out.println("Sending username to server...");
            name = nameField.getText();
            pw.println(name);
            // put clientListenThread here??
            new ClientListenThread(socket, name, this);
            System.out.println(name);
            System.out.println("Username received by server!");
            System.out.println("Waiting for remaining players to join...");
            joinButton.setText("Waiting for remaining players to join...");
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }

    private class ListenThread implements Runnable {
        public void run() {
            try {
                while(!socket.isClosed()) {
                    String message = br.readLine();
                    System.out.println(message);
                    if(message.equals("start")) {
                        frame.dispose();
                        clip.stop();
                        System.out.println("All players have joined!");
                        new PlayingScreen(socket, name);
                        System.out.println("PlayingScreen created");
                    }
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds\\StartScreen.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
