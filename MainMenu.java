import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

public class MainMenu implements ActionListener {
    private JFrame frame;
    private JTextField ipField, nameField;

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
        JButton joinButton = new JButton("JOIN");
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println("Trying to connect...");
            Socket socket = new Socket(ipField.getText(),55555);
            System.out.println("Connected!");
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Sending username to server...");
            pw.println(nameField.getText());
            frame.dispose();
            new PlayingScreen(socket);
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }
}