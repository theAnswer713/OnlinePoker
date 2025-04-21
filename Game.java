import java.util.ArrayList;
import java.util.TreeMap;

public class Game {
    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Player> winners;
    
    public Game(ArrayList<Player> players) {
        this.players = players;
        winners = new ArrayList<Player>();
        deck = new Deck();
        for(int i=0; i<10; i++) {
            deck.Shuffle();
        }
    }
    
    //deal hole cards
    public void holes() {
        for(int i=0; i<2; i++) {
            for(int j=0; j<players.size(); j++) {
                players.get(j).getHole().add(deck.dealCard());
                players.get(j).getHand().add(players.get(j).getHole().get(i));
            }
        }
    }
    //bet
    public void bet() {
        //fold, check, call, raise
        //if fold, set boolean false for player
        int highest = 0;
        boolean match = false;
        while(match==false) {
            for(int i=0; i<players.size(); i++) {
                if(players.get(i).isFolded()==false) {
                    //bet or fold or smth i suppose
                }
            }
            match = true;
            for(int i=0; i<players.size(); i++) {
                if(players.get(i).isFolded()==false && players.get(i).highBet()!=highest) {
                    match = false;
                }
            }
        }
    }
    //flop
    public void flop() {
        ArrayList<Card> flop = new ArrayList<Card>();
        for(int i=0; i<3; i++) {
            flop.add(deck.dealCard());
            for(int j=0; j<players.size(); j++) {
                players.get(j).getHand().add(flop.get(i));
            }
        }
    }
    //bet
    
    //turn
    public void turn() {
        Card turn = deck.dealCard();
        for(int i=0; i<players.size(); i++) {
            players.get(i).getHand().add(turn);
        }
    }
    //bet
    
    //river
    public void river() {
        Card river = deck.dealCard();
        for(int i=0; i<players.size(); i++) {
            players.get(i).getHand().add(river);
        }
    }
    //bet
    
    //compare
    public void compare() {
        TreeMap<String, Integer> hands = new TreeMap();
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).isFolded()==false) {
                hands.put(players.get(i).getName(), players.get(i).handValue());
            }
        }
    }
    //money to winner
    public void distribute() {
        
    }
}
