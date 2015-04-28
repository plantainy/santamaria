package george.knightmoves;

import java.util.HashMap;
import java.util.Map;

public class MoveMapHolder {
    private static final String VOWELS = "AEIO";
    private static final char[][] KEYPAD = new char[4][5];
    private static final Map<Character, GroupedKeyList> MOVE_MAP = new HashMap<>();
    static {
        KEYPAD[0] = new char[]{'A', 'B', 'C', 'D', 'E'};
        KEYPAD[1] = new char[]{'F', 'G', 'H', 'I', 'J'};
        KEYPAD[2] = new char[]{'K', 'L', 'M', 'N', 'O'};
        KEYPAD[3] = new char[]{'X', '1', '2', '3', 'X'};

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                char start = KEYPAD[i][j];
                if (start != 'X') {
                    safePut(MOVE_MAP, 'X', i, j);
                    safePut(MOVE_MAP, start, i - 2, j - 1);
                    safePut(MOVE_MAP, start, i - 2, j + 1);
                    safePut(MOVE_MAP, start, i - 1, j - 2);
                    safePut(MOVE_MAP, start, i - 1, j + 2);
                    safePut(MOVE_MAP, start, i + 1, j - 2);
                    safePut(MOVE_MAP, start, i + 1, j + 2);
                    safePut(MOVE_MAP, start, i + 2, j - 1);
                    safePut(MOVE_MAP, start, i + 2, j + 1);
                }
            }
        }
    }

    private static void safePut(Map<Character, GroupedKeyList> moveMap, char start, int i, int j) {
        if (i >= 0 && i < 4 && j >= 0 && j < 5) {
            char next = KEYPAD[i][j];
            if (next != 'X') {
                if (!moveMap.containsKey(start))
                    moveMap.put(start, new GroupedKeyList(2));
                moveMap.get(start).addKeyToGroup(next, getCost(next));
            }
        }
    }

    private static int getCost(char next) {
        return VOWELS.contains(String.valueOf(next)) ? 1 : 0;
    }

    public static Map<Character, GroupedKeyList> getMoveMap() {
        return new HashMap<>(MOVE_MAP);
    }
}
