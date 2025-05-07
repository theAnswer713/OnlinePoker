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

    public void shuffle() {
        Random rand = new Random();

        for(int count=0;count<1000;count++) {
            int split = deck.size() / 2 + rand.nextInt(9)-4;
            Stack<Card> l = new Stack<>();
            Stack<Card> r = new Stack<>();
            for(int i=0;i<split;i++) {
                l.push(deck.pop());
            }
            while(!deck.isEmpty()) {
                r.push(deck.pop());
            }
            while(!l.isEmpty()||!r.isEmpty()) {
                int n = Math.min(l.size(),rand.nextInt(3)+1);
                for(int i=0;i<n;i++) {
                    deck.push(l.pop());
                }

                n = Math.min(r.size(),rand.nextInt(3)+1);
                for(int i=0;i<n;i++) {
                    deck.push(r.pop());
                }
            }
        }
    }
}