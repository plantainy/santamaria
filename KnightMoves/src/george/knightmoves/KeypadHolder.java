package george.knightmoves;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeypadHolder {
    private static final List<Character> VOWELS = Arrays.asList('A', 'E', 'I', 'O');
    private static final char[][] KEYPAD = new char[4][5];
    private static final Map<Character, GroupedKeyList> moveMap = new HashMap<>();
    private static final Map<Character, GroupedKeyList> reducedMoveMap = new HashMap<>();

    static {
        KEYPAD[0] = new char[]{'A', 'B', 'C', 'D', 'E'};
        KEYPAD[1] = new char[]{'F', 'G', 'H', 'I', 'J'};
        KEYPAD[2] = new char[]{'K', 'L', 'M', 'N', 'O'};
        KEYPAD[3] = new char[]{'X', '1', '2', '3', 'X'};

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                char start = KEYPAD[i][j];
                if (start != 'X') {
                    safePut(moveMap, reducedMoveMap, start, i - 2, j - 1);
                    safePut(moveMap, reducedMoveMap, start, i - 2, j + 1);
                    safePut(moveMap, reducedMoveMap, start, i - 1, j - 2);
                    safePut(moveMap, reducedMoveMap, start, i - 1, j + 2);
                    safePut(moveMap, reducedMoveMap, start, i + 1, j - 2);
                    safePut(moveMap, reducedMoveMap, start, i + 1, j + 2);
                    safePut(moveMap, reducedMoveMap, start, i + 2, j - 1);
                    safePut(moveMap, reducedMoveMap, start, i + 2, j + 1);
                }
            }
        }
    }

    public static Map<Character, GroupedKeyList> getMoveMap() {
        return new HashMap<>(moveMap);
    }

    public static Map<Character, GroupedKeyList> getReducedMoveMap() {
        return new HashMap<>(reducedMoveMap);
    }

    private static void safePut(Map<Character, GroupedKeyList> moveMap,
                                Map<Character, GroupedKeyList> reducedMoveMap,
                                char start, int i, int j) {
        if (i >= 0 && i < 4 && j >= 0 && j < 5) {
            char end = KEYPAD[i][j];
            if (end != 'X') {
                if (!moveMap.containsKey(start)) {
                    moveMap.put(start, new GroupedKeyList(3));
                    reducedMoveMap.put(start, new GroupedKeyList(3));
                }
                int cost = getCost(start, end);
                int reducedCost = VOWELS.contains(start) ? cost - 1 : cost;
                moveMap.get(start).addKeyToGroup(end, cost);
                reducedMoveMap.get(start).addKeyToGroup(end, reducedCost);
            }
        }
    }

    private static int getCost(char start, char end) {
        return VOWELS.contains(start) ?
                VOWELS.contains(end) ? 2 : 1 :
                VOWELS.contains(end) ? 1 : 0;
    }
}
