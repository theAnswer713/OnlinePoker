import java.util.Stack;
import java.util.Random;
public class Deck {
    private Stack<Card> deck;
    public Deck() {
        deck = new Stack<Card>();
        for (int s=0; s<4; s++) {
            String suit = "";
            if(s==0) {suit="h";}
            if(s==1) {suit="d";}
            if(s==2) {suit="c";}
            if(s==3) {suit="s";}
            for (int v=2; v<=14; v++) {
                deck.push(new Card(v, suit));
            }
        }
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public Card dealCard() {
        return deck.pop();
    }

    public void Shuffle() {
        Random rand = new Random();
        Stack<Card> left = new Stack<Card>();
        Stack<Card> right = new Stack<Card>();
        int half = deck.size() / 2;
        for (int i=0; i<half; i++) {
            left.push(deck.pop());
        }
        while (deck.size() > 0) {
            right.push(deck.pop());
        }
        while(left.size() > 0 || right.size() > 0) {
            int num = rand.nextInt(3)+1;
            for (int i=0; i<num && left.size()>0; i++) {
                deck.push(left.pop());
            }
            num = rand.nextInt(3)+1;
            for (int i=0; i<num && right.size()>0; i++) {
                deck.push(right.pop());
            }
        }
    }
}