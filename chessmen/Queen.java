package chessmen;
public class Queen implements Piece {
    private int steps;

    public Queen () { this(1); }

    public Queen (int s) { this.steps = s; }

    public int [][] move () {
        return new int [][] {{steps, 0}, {-steps, -0}, {0, steps}, {0, -steps}, {steps, steps}, {steps, -steps}, {-steps, -steps}, {-steps, steps}};
    }
}
