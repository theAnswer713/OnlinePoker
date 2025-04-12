public class Card {
    private int value;
    private String suit;
    
    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getSuit() {
        return suit;
    }
    
    @Override
    public String toString() {
        if (value == 14) {
            return "A"+suit;
        }
        else if (value == 11) {
            return "J"+suit;
        }
        else if (value == 12) {
            return "Q"+suit;
        }
        else if (value == 13) {
            return "K"+suit;
        }
        else {
            return value+suit;
        }
    }
}
