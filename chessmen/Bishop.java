package chessmen;

public class Bishop implements Piece {
    private int steps;

    public Bishop () { this(1); }

    public Bishop (int s) { this.steps = s; }

    public int [][] move () {
        return new int [][] {{steps, steps}, {steps, -steps}, {-steps, -steps}, {-steps, steps}};
    }
}
