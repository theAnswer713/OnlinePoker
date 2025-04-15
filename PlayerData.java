import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerData {
    private Socket socket;
    private String username;
    private BufferedReader br;
    private PrintWriter pw;

    public PlayerData(Socket socket, String username) throws Exception {
        this.socket = socket;
        this.username = username;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream(), true);
    }

    public Socket getSocket() {
        return socket;
    }
    public String getUsername() {
        return username;
    }
    public BufferedReader getBr() {
        return br;
    }
    public PrintWriter getPw() {
        return pw;
    }
}
