import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Server {
    private ServerSocket server;
    private List<Player> players;
    private List<Card> tableCards;
    private Deck deck;
    private HashMap<String, Integer> hands;
    private HashMap<String, Integer> highBets;
    private HashMap<String, Integer> bets;
    private int pot;

    public static void main(String[] args) throws Exception {
        new Server();
    }

    public Server() throws Exception {
        this.server = new ServerSocket(55555);
        this.players = new ArrayList<Player>();
        this.tableCards = new ArrayList<Card>();
        this.deck = new Deck();
        this.hands = new HashMap();
        this.highBets = new HashMap();
        this.bets = new HashMap();
        this.pot = 0;

        Thread acceptThread = new Thread(new AcceptThread());
        acceptThread.start();
        acceptThread.join();
    }

    private class AcceptThread implements Runnable {
        public void run() {
            String nameList = "";
            try {
                while(!server.isClosed()) {
                    while(players.size()<4) {
                        System.out.println("Waiting for players to join...");
                        Socket socket = server.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String name = br.readLine();
                        nameList += name+"/";
                        Player player = new Player(name, socket);
                        players.add(player);

                        Thread listenThread = new Thread(new ListenThread(player));
                        listenThread.start();
                    }
                    System.out.println("All players have joined!");
                    nameList = nameList.substring(0, nameList.length()-1);
                    for(Player x:players) {
                        x.getPw().println("start");
                        x.getPw().println(nameList);
                    }
                    Thread gameThread = new Thread(new GameThread());
                    gameThread.start();
                    Thread.currentThread().interrupt();
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    private class ListenThread implements Runnable {
        private Player player;

        public ListenThread(Player player) {
            this.player = player;
        }

        public void run() {
            try {
                while(!player.getSocket().isClosed()) {
                    String message = player.getBr().readLine();
                    System.out.println(message);
                    for(Player x: players) {
                        x.getPw().println(message);
                    }
                }
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    private class GameThread implements Runnable {
        public void run() {
            try {
                for (Player player : players) {
                    for (int i = 0; i < players.size(); i++) {
                        Card c1 = players.get(i).getHand().get(0);
                        Card c2 = players.get(i).getHand().get(1);
                        String cardNames = c1.getSuit() + c1.getValue() + "/" + c2.getSuit() + c2.getValue();
                        player.getPw().println(cardNames);
                    }
                }
                for (Player player : players) {
                    bets.put(player.getName(), 0);
                }
                for (int i = 0; i < 10; i++) {
                    deck.Shuffle();
                }
                deal();
                bet();
                flop();
                bet();
                turn();
                bet();
                turn();
                bet();
                compare();
                distribute();
            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void deal() {
        for(int i=0; i<2; i++) {
            for (Player player:players) {
                player.getHole().add(deck.dealCard());
                player.getHand().add(player.getHole().get(i));
            }

        }
    }

    public void bet() throws Exception {
        //fold, check, call, raise
        //if fold, set boolean false for player
        for (Player player : players) {
            highBets.put(player.getName(), 0);
        }
        int highest = 0;
        boolean match = false;
        while(match==false) {
            for(int i=0; i<players.size(); i++) {
                if(players.get(i).isFolded()==false) {
                    //open options to player
                    //amount is standin for however much is bet
                    //yay networking go luca
                    String move = players.get(i).getBr().readLine();
                    if(move.equals("fold")) {
                        players.get(i).fold();
                        for(Player player:players) {
                            player.getPw().println("fold"+i);
                        }
                    }

                    //now do this for the other buttons!!

                    int amount = 0;
                    bets.put(players.get(i).getName(), amount);
                    if(amount > highBets.get(players.get(i).getName())) {
                        highBets.put(players.get(i).getName(), amount);
                    }
                    if(amount > highest) {
                        highest = amount;
                    }
                }
            }
            match = true;
            for (Player player : players) {
                if (player.isFolded() == false && highBets.get(player.getName()) != highest && player.getMoney() > 0) {
                    match = false;
                }
            }
        }
    }

    public void flop() {
        for(int i=0; i<3; i++) {
            tableCards.add(deck.dealCard());
            for (Player player : players) {
                player.getHand().add(tableCards.get(i));
            }
        }
    }

    public void turn() {
        Card turn = deck.dealCard();
        tableCards.add(turn);
        for (Player player : players) {
            player.getHand().add(turn);
        }
    }

    public void compare() {
        for (Player player : players) {
            if (player.isFolded() == false) {
                hands.put(player.getName(), player.handValue());
            }
        }
    }

    public ArrayList<String> bestHand() {
        int best = 0;
        ArrayList<String> bestName = new ArrayList<String>();
        for (Player player : players) {
            if (hands.get(player.getName()) > best) {
                best = hands.get(player.getName());
                bestName.add(player.getName());
            } else if (hands.get(player.getName()) == best) {
                bestName.add(player.getName());
            }
        }
        return bestName;
    }

    public ArrayList<String> sortBest() {
        ArrayList<String> bestHand = new ArrayList<String>(this.bestHand());
        for(int j=0; j<bestHand.size()+1; j++) {
            for(int i=1; i<bestHand.size(); i++) {
                if(bets.get(bestHand.get(i)) < bets.get(bestHand.get(i-1))) {
                    bestHand.add(i-1, bestHand.remove(i));
                }
            }
        }
        return bestHand;
    }

    public void distribute() {
        ArrayList<String> bestHand = new ArrayList<String>();
        while(pot>0) {
            bestHand = this.bestHand();
            if(bestHand.size()==1) {
                for (Player player : players) {
                    int amount = Math.min(bets.get(player.getName()), bets.get(bestHand.get(0)));
                    bets.put(player.getName(), (bets.get(player.getName()) - amount));
                    bets.put(bestHand.get(0), (bets.get(bestHand.get(0)) + amount));
                }
                for (Player player : players) {
                    if (bestHand.get(0).equals(player.getName())) {
                        player.setMoney(player.getMoney() + bets.get(bestHand.get(0)));
                        bets.put(bestHand.get(0), 0);
                        hands.remove(bestHand.get(0));
                    }
                }
            }
            else if(bestHand.size()>1) {
                bestHand = this.sortBest();
                for(int i=0; i<players.size(); i++) {
                    int amount = Math.min(bets.get(players.get(i).getName()), bets.get(bestHand.get(0)));
                    bets.put(players.get(i).getName(), (bets.get(players.get(i).getName())-amount));
                    while(amount > 0) {
                        for(int j=0; j<bestHand.size(); j++) {
                            bets.put(bestHand.get(j), bets.get(bestHand.get(j))+1);
                        }
                    }
                    for(int k=0; k<bestHand.size(); k++) {
                        for(int w=0; w<players.size(); w++) {
                            if(bestHand.get(k).equals(players.get(w).getName())) {
                                players.get(w).setMoney(players.get(w).getMoney()+bets.get(bestHand.get(k)));
                                bets.put(bestHand.get(k), 0);
                                hands.remove(bestHand.get(k));
                            }
                        }
                    }
                }
            }
        }
        for (Player player : players) {
            bets.put(player.getName(), 0);
            hands.put(player.getName(), 0);
            while (player.getHand().size() > 0) {
                player.getHand().remove(0);
            }
            while (player.getUsed().size() > 0) {
                player.getUsed().remove(0);
            }
            while (player.getHole().size() > 0) {
                player.getHole().remove(0);
            }
        }
    }
}
