package george.knightmoves;

import org.ejml.simple.SimpleMatrix;

/**
 * KeypadInitializer is a utility class that creates three collections based on the given keypad.
 * <ul>
 *     <li> Base mats:
 *          a matrix group {M0, M1} that contains initial single move matrices.
 *          The group index indicates the cost of the moves in the matrix, e.g., M0 contains all moves of cost 0. </li>
 *     <li> Front vectors:
 *          a row vector group that contains single moves from an invisible starting key to all keys on keypad.
 *          The group index indicates the cost, in the same manner as base mats. </li>
 *     <li> Sum vector:
 *          a column vector of 1's used to calculate the sum of all elements in a row vector. </li>
 * </ul>
 * As this initializer is only used by KnightMoveAlgo,
 * some security and performance concerns are ignored for simplicity
 * (e.g., the expensive initBaseMats method will not be called more than once).
 */
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

    /**
     * Initialize the single move matrix group based on the keypad and grouped by cost.
     * It returns a group of two matrices: {M0, M1}, which contains the moves of costs 0 and 1 respectively. <br/>
     * The cost is defined as follows:
     * <ul>
     *     <li> single move from any key to vowel: cost 1 </li>
     *     <li> else (including from vowel to non-vowel): cost 0 </li>
     * </ul>
     * @return matrix group of the single moves.
     */
    public static MatrixGroup initBaseMats() {
        MatrixGroup baseMats = new MatrixGroup(2, KEYS.length(), KEYS.length());
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

    /**
     * Initialize a row vector group for initial moves
     * from a invisible starting key to all keys on keypad, grouped by cost.
     * @return vector group of the single moves to all keys on keypad.
     */
    public static MatrixGroup initFrontVectors() {
        MatrixGroup frontVectors = new MatrixGroup(2, 1, KEYS.length());
        for (char c : KEYS.toCharArray())
            frontVectors.matAt(getCost(c)).set(0, KEYS.indexOf(c), 1);
        return frontVectors;
    }

    /**
     * Initialize a column vector of 1's used to calculate the sum of all elements in a row vector by multiplication.
     * @return a column vector of 1's
     */
    public static SimpleMatrix initSumVector() {
        SimpleMatrix sumVector = new SimpleMatrix(KEYS.length(), 1);
        for (int i = 0; i < sumVector.numRows(); i++)
            sumVector.set(i, 0, 1);
        return sumVector;
    }

    private static void safePut(MatrixGroup baseMats, char from, int i, int j) {
        if (i >= 0 && i < 4 && j >= 0 && j < 5) {
            char to = KEYPAD[i][j];
            if (to != 'X')
                baseMats.matAt(getCost(to)).set(KEYS.indexOf(from), KEYS.indexOf(to), 1);
        }
    }

    private static int getCost(char c) {
        return VOWELS.contains(String.valueOf(c)) ? 1 : 0;
    }
}
