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
        winners.add(players.get(0));
        for(int i=1; i<players.size(); i++) {
            if(players.get(i).handValue()==winners.get(0).handValue() && players.get(i).isFolded()==false) {
                winners.add(players.get(i));
            }
            else if(players.get(i).handValue()>winners.get(0).handValue()) {
                while(winners.size()>0) {
                    winners.remove(0);
                }
                winners.add(players.get(i));
            }
        }
        if(winners.size()>1) {
            for(int i=0; i<5; i++) {
                for(int j=0; j<winners.size(); j++) {
                    if(winners.size()==1) {
                        break;
                    }
                    else if(winners.get(j).getUsed().get(i).getValue() > winners.get(j+1).getUsed().get(i).getValue()){
                        winners.remove(j+1);
                        j--;
                    }
                    else if(winners.get(j).getUsed().get(i).getValue() < winners.get(j+1).getUsed().get(i).getValue()) {
                        winners.remove(j);
                        j--;
                    }
                }
            }
        }
    }
    //money to winner
    public void distribute() {
        
    }
}