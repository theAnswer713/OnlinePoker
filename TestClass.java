import java.net.Socket;

public class TestClass {
    public static void main(String[] args) throws Exception {

        /**
        Socket socket = new Socket("10.105.72.161",55555);

        new PlayingScreen(socket, "Luca");
         **/
        new MainMenu();
        /**
        Deck deck = new Deck();
        for (int i = 0; i < 10; i++) {
            deck.shuffle();
            System.out.println(deck.getDeck().size());
        }
        System.out.println("Shuffling complete. Number of cards: "+deck.getDeck().size());
         **/
    }
}
