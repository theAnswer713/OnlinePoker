import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

public class MainMenu implements ActionListener {
    private JFrame window;
    private JTextField ipField, nameField;

    public MainMenu() {
        window = new JFrame("Poker");

        ipField = new JTextField();
        nameField = new JTextField();

        JButton joinButton = new JButton("JOIN");
        joinButton.addActionListener(this);

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
            window.dispose();
            new PlayingScreen(socket);
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }
}