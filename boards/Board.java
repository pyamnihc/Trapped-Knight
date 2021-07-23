package boards;
import java.util.Set;

public interface Board {
    Set<Integer> pos2weights (int [][] positions);
    int[] weight2pos (int weight);
}
