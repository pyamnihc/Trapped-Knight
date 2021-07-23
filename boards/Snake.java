package boards;
import java.util.Set;
import java.util.HashSet;

public class Snake implements Board {
    private int pos2wt (int position[]) {
        int i = position[0];
        int j = position[1];
        int diagonal = i+j;
        int min = (diagonal*(diagonal+1))/2;
        return 1 + (diagonal%2==0 ? min + i : min + j);
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
        int steps = (diag*(diag+1))/2 - (int)weight;
        if (diag % 2 == 0)
            return new int [] {steps, diag - 1 - steps};
        else
            return new int [] {diag - 1 - steps, steps};

    }


}
