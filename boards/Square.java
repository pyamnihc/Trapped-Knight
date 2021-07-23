package boards;
import java.util.Set;
import java.util.HashSet;

public class Square implements Board {
    private int pos2wt (int position[]) {
        boolean dir = position[1] % 2 == 0;
        return  (1 - position[1])*(1 - position[1]) + (dir ? +1 : -1)*position[0];
    }

    public Set<Integer> pos2weights (int [][] positions) {
        Set<Integer> weights = new HashSet<Integer>();
        int x, y, lt, rt;
        for (int i = 0; i < positions.length; i++) {
            x = positions[i][0];
            y = positions[i][1];
            boolean dir = y % 2 == 0;
            if (dir) {
                lt = y;
                rt = -y + 1;
            }
            else {
                lt = y - 1;
                rt = -y;
            }

            if (y <= 0 && x <= rt  && x >= lt) {
                weights.add(pos2wt(positions[i]));
            }
            else continue;
        }
        return weights;
    }

    public int[] weight2pos (int weight) {
        int depth = (int)Math.floor(Math.sqrt(weight));
        int mid = depth*depth + depth;
        boolean dir = depth % 2 == 1;
        if (mid >= weight) {
            return new int [] {(dir ? 1 : -1)*(depth - mid + weight), -depth + 1};
        }
        else {

            return new int [] {(dir ? 1 : -1)*(depth - weight + mid + 1), -depth};
        }
    }

}
