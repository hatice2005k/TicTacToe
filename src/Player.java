
public class Player {

    private final char symbol;
    private int score;

    // Constructor
    public Player(char symbol) {
        this.symbol = symbol;
        this.score = 0;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore() {
        score++;
    }

    public void resetScore() {
        score = 0;
    }
}
