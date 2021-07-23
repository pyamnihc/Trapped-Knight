package chessmen;
public class Rook implements Piece {
    private int steps;

    public Rook () { this(1); }
    public Rook (int s) {this.steps = 1;}

    public int [][] move () {
        return new int [][] {{steps, 0}, {-steps, -0}, {0, steps}, {0, -steps}};
    }
} 
