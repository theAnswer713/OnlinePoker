import java.util.ArrayList;
import java.util.Scanner;
public class Player {
    private String name;
    private ArrayList<Card> hole;
    private ArrayList<Card> hand;
    private ArrayList<Card> used;
    
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<Card>();
        this.used = new ArrayList<Card>();
        this.hole = new ArrayList<Card>();
    }
    
    public String getName() {
        return name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Card> getUsed() {
        return used;
    }

    public ArrayList<Card> getHole() {
        return hole;
    }
    
    public ArrayList<Card> sortHand() {
        for(int i=0; i<hand.size(); i++) {
            for(int j=0; j<hand.size()-1; j++) {
                if(hand.get(j).getValue() < hand.get(j+1).getValue()) {
                    Card temp = new Card(hand.get(j).getValue(), hand.get(j).getSuit());
                    hand.set(j, hand.get(j+1));
                    hand.set(j+1, temp);
                }
            }
        }
        return hand;
    }
    
    public ArrayList<Card> sortUsed() {
        for(int i=0; i<used.size(); i++) {
            for(int j=0; j<used.size()-1; j++) {
                if(used.get(j).getValue() < used.get(j+1).getValue()) {
                    Card temp = new Card(used.get(j).getValue(), used.get(j).getSuit());
                    used.set(j, hand.get(j+1));
                    used.set(j+1, temp);
                }
            }
        }
        return used;
    }
    
    public boolean isPair() {
        for(int i=0; i<hand.size()-1; i++) {
            if(hand.get(i).getValue()==hand.get(i+1).getValue()){
                for(int j=0; j<2; j++) {
                    used.add(hand.remove(i));
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean isThree() {
        for(int i=0; i<5; i++) {
            if(hand.get(i).getValue()==hand.get(i+2).getValue()){
                for(int j=0; j<3; j++) {
                    used.add(hand.remove(i));
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean isFour() {
        for(int i=0; i<4; i++) {
            if(hand.get(i).getValue()==hand.get(i+3).getValue()){
                for(int j=0; j<4; j++) {
                    used.add(hand.remove(i));
                }
                used.add(hand.remove(0));
                return true;
            }
        }
        return false;
    }
    
    public boolean isFullHouse() {
        if(this.isThree()) {
            if(this.isPair()) {
                return true;
            }
            else {
                while(used.size()>0) {
                    hand.add(used.remove(0));
                }
                this.sortHand();
            }
        }
        return false;
    }
    
    public boolean isTwoPair() {
        if(this.isPair()) {
            if(this.isPair()) {
                used.add(hand.remove(0));
                return true;
            }
            else {
                while(used.size()>0) {
                    hand.add(used.remove(0));
                }
                this.sortHand();
            }
        }
        return false;
    }
    
    public boolean isStraight() {
        for(int i=0; i<=2; i++) {
            used.add(hand.remove(i));
            for(int j=i; j<hand.size(); j++) {
                if(hand.get(j).getValue()==(used.get(used.size()-1).getValue()-1) && used.size()<5) {
                    used.add(hand.remove(j));
                    j--;
                }
            }
            if(used.size()<5) {
                while(used.size()>0) {
                    hand.add(used.remove(0));
                }
                this.sortHand();
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    public boolean isFlush() {
        for(int i=0; i<=2; i++) {
            used.add(hand.remove(i));
            for(int j=i; j<hand.size(); j++) {
                if(hand.get(j).getSuit()==(used.get(used.size()-1).getSuit()) && used.size()<5) {
                    used.add(hand.remove(j));
                    j--;
                }
            }
            if(used.size()<5) {
                while(used.size()>0) {
                    hand.add(used.remove(0));
                }
                this.sortHand();
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSF() {
        for(int i=0; i<=2; i++) {
            used.add(hand.remove(i));
            for(int j=i; j<hand.size(); j++) {
                if(hand.get(j).getValue()==(used.get(used.size()-1).getValue()-1) && hand.get(j).getSuit()==used.get(used.size()-1).getSuit() && used.size()<5) {
                    used.add(hand.remove(j));
                    j--;
                }
            }
            if(used.size()<5) {
                while(used.size()>0) {
                    hand.add(used.remove(0));
                }
                this.sortHand();
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    public int handValue() {
        if(this.isSF()) {
            return 9;
        }
        else if(this.isFour()) {
            return 8;
        }
        else if(this.isFullHouse()) {
            return 7;
        }
        else if(this.isFlush()) {
            return 6;
        }
        else if(this.isStraight()) {
            return 5;
        }
        else if(this.isThree()) {
            for(int i=0; i<2; i++) {
                used.add(hand.remove(0));
            }
            return 4;
        }
        else if(this.isTwoPair()) {
            return 3;
        }
        else if(this.isPair()) {
            for(int i=0; i<3; i++) {
                used.add(hand.remove(0));
            }
            return 2;
        }
        else {
            for(int i=0; i<5; i++) {
                used.add(hand.remove(0));
            }
            return 1;
        }
    }
    
    @Override
    public String toString() {
        return name+" "+hand+" "+used;
    }
}
