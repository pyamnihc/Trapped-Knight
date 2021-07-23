package chessmen;

public class Knight implements Piece {
    private int B;
    private int S;

    public Knight () { this(2, 1); }

    public Knight (int b, int s) {
        this.B = b;
        this.S = s;
    }

    public int [][] move() {
        return new int [][] {{B, S}, {B, -S}, {-B, S}, {-B, -S}, {S, B}, {-S, B}, {S, -B}, {-S, -B}};    
    }
}
