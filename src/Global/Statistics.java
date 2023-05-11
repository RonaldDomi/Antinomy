package Global;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    public static int currentDepthTotalConfigurations = 0;
    private static Map<Integer, List<Integer>> statistics = new HashMap<>();
    private static Map<Integer, List<Integer>> statisticsWithAlphaBeta = new HashMap<>();

    public static void addDepth(int depth, int number, boolean withAlphaBeta) {
        if(withAlphaBeta){
            if (!statisticsWithAlphaBeta.containsKey(depth)) {
                statisticsWithAlphaBeta.put(depth, new ArrayList<>());
            }
            statisticsWithAlphaBeta.get(depth).add(number);
            return;
        }

        // no alpha beta
        if (!statistics.containsKey(depth)) {
            statistics.put(depth, new ArrayList<>());
        }
        statistics.get(depth).add(number);
    }

    public static List<Integer> getDepthList(int depth, boolean withAlphaBeta) {
        if(withAlphaBeta) {
            if (statisticsWithAlphaBeta.containsKey(depth)) {
                return statisticsWithAlphaBeta.get(depth);
            } else {
                return new ArrayList<>();
            }
        }

//      no alpha beta
        if (statistics.containsKey(depth)) {
            return statistics.get(depth);
        } else {
            return new ArrayList<>();
        }
    }

    public static void setCurrentDepthTotalConfigurations(int newDepthTotalConfigurations) {
        currentDepthTotalConfigurations = newDepthTotalConfigurations;
    }

    public static double getAverageForDepth(int depth, boolean withAlphaBeta){
        List<Integer> numbers;
        numbers = (withAlphaBeta) ? getDepthList(depth, true) : getDepthList(depth, false);
        if (numbers == null || numbers.isEmpty()) {
            return 0.0; // Return 0 if the list is empty or null
        }

        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }

        return (double) sum / numbers.size();
    }

    public static void incrementConfigurationsLookedAt(){
        currentDepthTotalConfigurations++;
    }


}
