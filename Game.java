import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Game {
    private Player[] players;
    private Deck deck;
    private HashMap<String, Integer> hands;
    private HashMap<String, Integer> highBets;
    private HashMap<String, Integer> bets;
    private int pot;
    private Socket socket;
    private BufferedReader br;
    
    //ALL METHODS SHOULD (I THINK) BE RUN ON THE SERVER AND THEN INFORMATION SENT TO PLAYERS
    
    public Game(Player[] players, Socket socket) throws Exception {
        this.players = players;
        deck = new Deck();
        hands = new HashMap();
        highBets = new HashMap();
        bets = new HashMap();
        pot = 0;
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        for(int i=0; i<players.length; i++) {
            bets.put(players[i].getName(), 0);
        }
        for(int i=0; i<10; i++) {
            deck.Shuffle();
        }
        deal();
        bet();
        flop();
    }
    
    //deal hole cards
    public void deal() {
        for(int i=0; i<2; i++) {
            for(int j=0; j<players.length; j++) {
                players[j].getHole().add(deck.dealCard());
                players[j].getHand().add(players[j].getHole().get(i));
            }
        }
    }

    //bet
    //ts not done yet gang
    public void bet() {
        //fold, check, call, raise
        //if fold, set boolean false for player
        for(int i=0; i<players.length; i++) {
            highBets.put(players[i].getName(), 0);
        }
        int highest = 0;
        boolean match = false;
        while(match==false) {
            for(int i=0; i<players.length; i++) {
                if(players[i].isFolded()==false) {
                    //open options to player
                    //amount is standin for however much is bet
                    //yay networking go luca



                    int amount = 0;
                    bets.put(players[i].getName(), amount);
                    if(amount > highBets.get(players[i].getName())) {
                        highBets.put(players[i].getName(), amount);
                    }
                    if(amount > highest) {
                        highest = amount;
                    }
                }
            }
            match = true;  //temporary
            for(int i=0; i<players.length; i++) {
                if(players[i].isFolded()==false && (highBets.get(players[i].getName())!=highest || players[i].getMoney()>0)) {
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
            for(int j=0; j<players.length; j++) {
                players[j].getHand().add(flop.get(i));
            }
        }
    }
    //bet


    //turn
    public void turn() {
        Card turn = deck.dealCard();
        for(int i=0; i<players.length; i++) {
            players[i].getHand().add(turn);
        }
    }
    //bet
    
    //compare
    public void compare() {
        for(int i=0; i<players.length; i++) {
            if(players[i].isFolded()==false) {
                hands.put(players[i].getName(), players[i].handValue());
            }
        }
    }
    //called when distrubte
    public ArrayList<String> bestHand() {
        int best = 0;
        ArrayList<String> bestName = new ArrayList<String>();
        for(int i=0; i<players.length; i++) {
            if(hands.get(players[i].getName()) > best) {
                best = hands.get(players[i].getName());
                bestName.add(players[i].getName());
            }
            else if(hands.get(players[i].getName()) == best) {
                bestName.add(players[i].getName());
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
                for(int i=0; i<players.length; i++) {
                    int amount = Math.min(bets.get(players[i].getName()), bets.get(bestHand.get(0)));
                    bets.put(players[i].getName(), (bets.get(players[i].getName())-amount));
                    bets.put(bestHand.get(0), (bets.get(bestHand.get(0))+amount));
                }
                for(int i=0; i<players.length; i++) {
                    if(bestHand.get(0).equals(players[i].getName())) {
                        players[i].setMoney(players[i].getMoney()+bets.get(bestHand.get(0)));
                        bets.put(bestHand.get(0), 0);
                        hands.remove(bestHand.get(0));
                    }
                }
            }
            else if(bestHand.size()>1) {
                bestHand = this.sortBest();
                for(int i=0; i<players.length; i++) {
                    int amount = Math.min(bets.get(players[i].getName()), bets.get(bestHand.get(0)));
                    bets.put(players[i].getName(), (bets.get(players[i].getName())-amount));
                    while(amount > 0) {
                        for(int j=0; j<bestHand.size(); j++) {
                            bets.put(bestHand.get(j), bets.get(bestHand.get(j))+1);
                        }
                    }
                    for(int k=0; k<bestHand.size(); k++) {
                        for(int w=0; w<players.length; w++) {
                            if(bestHand.get(k).equals(players[w].getName())) {
                                players[w].setMoney(players[w].getMoney()+bets.get(bestHand.get(k)));
                                bets.put(bestHand.get(k), 0);
                                hands.remove(bestHand.get(k));
                            }
                        }
                    }
                }
            }
        }
        for(int i=0; i<players.length; i++) {
            bets.put(players[i].getName(), 0);
            hands.put(players[i].getName(), 0);
            while(players[i].getHand().size()>0) {
                players[i].getHand().remove(0);
            }
            while(players[i].getUsed().size()>0) {
                players[i].getUsed().remove(0);
            }
            while(players[i].getHole().size()>0) {
                players[i].getHole().remove(0);
            }
        }
    }
}
