package george.knightmoves;

import org.ejml.simple.SimpleMatrix;

public class KeypadInitializer {
    private static final String KEYS = "ABCDEFGHIJKLMNO123";
    private static final String VOWELS = "AEIO";
    private static final char[][] KEYPAD = new char[4][5];
    static {
        KEYPAD[0] = new char[]{'A', 'B', 'C', 'D', 'E'};
        KEYPAD[1] = new char[]{'F', 'G', 'H', 'I', 'J'};
        KEYPAD[2] = new char[]{'K', 'L', 'M', 'N', 'O'};
        KEYPAD[3] = new char[]{'X', '1', '2', '3', 'X'};
    }

    public static int getNumKeys() {
        return KEYS.length();
    }

    public static SimpleMatrix initSumVector() {
        SimpleMatrix sumVector = new SimpleMatrix(KEYS.length(), 1);
        for (int i = 0; i < sumVector.numRows(); i++)
            sumVector.set(i, 0, 1);
        return sumVector;
    }

    public static SimpleMatrix[] initFrontVectors() {
        SimpleMatrix[] frontVectors = new SimpleMatrix[3];
        int numKeys = KEYS.length();
        for (int i = 0; i < frontVectors.length; i++)
            frontVectors[i] = new SimpleMatrix(1, numKeys);
        for (char c : KEYS.toCharArray())
            frontVectors[getCost(c)].set(0, KEYS.indexOf(c), 1);
        return frontVectors;
    }

    public static SimpleMatrix[] initBaseMats() {
        SimpleMatrix[] baseMats = new SimpleMatrix[3];
        int numKeys = KEYS.length();
        for (int i = 0; i < baseMats.length; i++)
            baseMats[i] = new SimpleMatrix(numKeys, numKeys);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                char from = KEYPAD[i][j];
                if (from != 'X') {
                    safePut(baseMats, from, i - 2, j - 1);
                    safePut(baseMats, from, i - 2, j + 1);
                    safePut(baseMats, from, i - 1, j - 2);
                    safePut(baseMats, from, i - 1, j + 2);
                    safePut(baseMats, from, i + 1, j - 2);
                    safePut(baseMats, from, i + 1, j + 2);
                    safePut(baseMats, from, i + 2, j - 1);
                    safePut(baseMats, from, i + 2, j + 1);
                }
            }
        }
        return baseMats;
    }

    private static void safePut(SimpleMatrix[] baseMats, char from, int i, int j) {
        if (i >= 0 && i < 4 && j >= 0 && j < 5) {
            char to = KEYPAD[i][j];
            if (to != 'X')
                baseMats[getCost(to)].set(KEYS.indexOf(from), KEYS.indexOf(to), 1);
        }
    }

    private static int getCost(char c) {
        return VOWELS.contains(String.valueOf(c)) ? 1 : 0;
    }
}
