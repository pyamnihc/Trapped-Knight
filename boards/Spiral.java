package boards;
import java.util.Set;
import java.util.HashSet;

public class Spiral implements Board {
    private int pos2wt (int position[]) {
        int weight = 1;
        int i = Math.abs(position[0]) > Math.abs(position[1]) ? 0 : 1;
        int n = Math.abs(position[i]);
        switch (i) {
            case 0: switch ((int)Math.signum(position[i])) {
                        case -1: weight += 5*n + position[1];
                                 break;
                        case 1: weight += n - position[1]; 
                                break;
                        default: break;
                    }
                    break;

            case 1: switch ((int)Math.signum(position[i])) {
                        case -1: weight += 3*n - position[0];
                                 break;
                        case 1: weight += 7*n + position[0];
                                break; 
                        default: break;        
                    }
                    break;

            default:
                    break;
        }

        weight += (8 *n*(n-1))/2;
        return weight;   
    }

    public Set<Integer> pos2weights (int [][] positions) {
        Set<Integer> weights = new HashSet<Integer>();
        for (int i = 0; i < positions.length; i++) {
            weights.add(pos2wt(positions[i]));
        }
        return weights;
    }
   
    public int [] weight2pos(int n) {
        double sq = Math.ceil((Math.sqrt(n) - 1) / 2);
        double len = 2 * sq + 1;
        double max = len * len;
        len = len - 1;
        if (n >= max - len)
            return new int [] {(int) (sq - (max - n)), (int) (sq)};
        else
            max = max - len;
        if (n >= max - len)
            return new int [] {(int) -sq, (int) (sq - (max - n))};
        else
            max = max - len;
        if (n >= max - len)
            return new int [] {(int) (-sq + (max - n)), (int) (-sq)};
        else
            return new int [] {(int) sq, (int) (-sq + (max - n - len))};
    }


}
