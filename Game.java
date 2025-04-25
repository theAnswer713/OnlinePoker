import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private ArrayList<Player> players;
    private Deck deck;
    private HashMap<String, Integer> hands;
    private HashMap<String, Integer> highBets;
    private HashMap<String, Integer> bets;
    private int pot;
    
    //ALL METHODS SHOULD (I THINK) BE RUN ON THE SERVER AND THEN INFORMATION SENT TO PLAYERS
    
    public Game(ArrayList<Player> players) {
        this.players = players;
        deck = new Deck();
        hands = new HashMap();
        highBets = new HashMap();
        bets = new HashMap();
        pot = 0;
        for(int i=0; i<players.size(); i++) {
            bets.put(players.get(i).getName(), 0);
        }
        for(int i=0; i<10; i++) {
            deck.Shuffle();
        }
    }
    
    //deal hole cards
    public void deal() {
        for(int i=0; i<2; i++) {
            for(int j=0; j<players.size(); j++) {
                players.get(j).getHole().add(deck.dealCard());
                players.get(j).getHand().add(players.get(j).getHole().get(i));
            }
        }
    }
    //bet
    //ts not done yet gang
    public void bet() {
        //fold, check, call, raise
        //if fold, set boolean false for player
        for(int i=0; i<players.size(); i++) {
            highBets.put(players.get(i).getName(), 0);
        }
        int highest = 0;
        boolean match = false;
        while(match==false) {
            for(int i=0; i<players.size(); i++) {
                if(players.get(i).isFolded()==false) {
                    //open options to player
                    //amount is standin for however much is bet
                    //yay networking go luca
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
            for(int i=0; i<players.size(); i++) {
                if(players.get(i).isFolded()==false && highBets.get(players.get(i).getName())!=highest && players.get(i).getMoney()>0) {
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
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).isFolded()==false) {
                hands.put(players.get(i).getName(), players.get(i).handValue());
            }
        }
    }
    //called when distrubte
    public ArrayList<String> bestHand() {
        int best = 0;
        ArrayList<String> bestName = new ArrayList<String>();
        for(int i=0; i<players.size(); i++) {
            if(hands.get(players.get(i).getName()) > best) {
                best = hands.get(players.get(i).getName());
                bestName.add(players.get(i).getName());
            }
            else if(hands.get(players.get(i).getName()) == best) {
                bestName.add(players.get(i).getName());
            }
        }
        return bestName;
    }
    //called when distrubte
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
    //money to winner
    public void distribute() {
        ArrayList<String> bestHand = new ArrayList<String>();
        while(pot>0) {
            bestHand = this.bestHand();
            if(bestHand.size()==1) {
                for(int i=0; i<players.size(); i++) {
                    int amount = Math.min(bets.get(players.get(i).getName()), bets.get(bestHand.get(0)));
                    bets.put(players.get(i).getName(), (bets.get(players.get(i).getName())-amount));
                    bets.put(bestHand.get(0), (bets.get(bestHand.get(0))+amount));
                }
                for(int i=0; i<players.size(); i++) {
                    if(bestHand.get(0).equals(players.get(i).getName())) {
                        players.get(i).setMoney(players.get(i).getMoney()+bets.get(bestHand.get(0)));
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
        for(int i=0; i<players.size(); i++) {
            bets.put(players.get(i).getName(), 0);
            hands.put(players.get(i).getName(), 0);
            while(players.get(i).getHand().size()>0) {
                players.get(i).getHand().remove(0);
            }
            while(players.get(i).getUsed().size()>0) {
                players.get(i).getUsed().remove(0);
            }
            while(players.get(i).getHole().size()>0) {
                players.get(i).getHole().remove(0);
            }
        }
    }
}
