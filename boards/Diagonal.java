package boards;
import java.util.Set;
import java.util.HashSet;

public class Diagonal implements Board {
    private int pos2wt (int position[]) {
        int i = position[0];
        int j = position[1];
        return 1 + ((j+1)*(j+2))/2 - 1 + i*j + (i*(i+1))/2;   
    }

    public Set<Integer> pos2weights (int [][] positions) {
        Set<Integer> weights = new HashSet<Integer>();
        for (int i = 0; i < positions.length; i++) {
            if (positions[i][0] >= 0 && positions[i][1] >= 0) {
                weights.add(pos2wt(positions[i]));
            }
            else continue;
        }
        return weights;
    }

    public int[] weight2pos (int weight) {
        int diag = (int)Math.ceil((Math.sqrt(1 + 8*weight) - 1)/2.);
        int steps = (diag*(diag+1))/2 - weight;

        return new int [] {steps, diag - 1 - steps};

    }
 
}
