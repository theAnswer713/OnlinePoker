import javax.swing.*;
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
    }
}