package george.knightmoves;

import org.ejml.simple.SimpleMatrix;

/**
 * KeypadInitializer is a utility class that creates three collections based on the given keypad.
 * <ul>
 *     <li> baseMats: a matrix group {M0, M1} that contains initial single move matrices.
 *          The group index indicates the cost of the moves in the matrix, e.g., M0 contains all moves of cost 0. </li>
 *     <li> sumVector: a row vector of 1's used to calculate the sum of elements in a matrix. </li>
 *     <li> reduceVector: similar to sumVector - a row vector of 1's
 *          except that at the positions corresponding to vowels, the element is 0. </li>
 * </ul>
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

    private static MatrixGroup baseMats;
    private static SimpleMatrix sumVector;
    private static SimpleMatrix reduceVector;

    public static int getNumKeys() {
        return KEYS.length();
    }

    public static MatrixGroup getBaseMats() {
        if (baseMats == null)
            initBaseMats();
        return baseMats;
    }

    public static SimpleMatrix getSumVector() {
        if (sumVector == null)
            initSumAndReduceVectors();
        return sumVector;
    }

    public static SimpleMatrix getReduceVector() {
        if (reduceVector == null)
            initSumAndReduceVectors();
        return reduceVector;
    }

    /**
     * Initialize baseMats - the single move matrix group based on the keypad and grouped by cost.
     * baseMats is a group of two matrices: {M0, M1}, which contains the moves of costs 0 and 1 respectively. <br/>
     * The cost is defined as follows:
     * <ul>
     *     <li> single move from any key to a vowel: cost 1 </li>
     *     <li> else (including from vowel to non-vowel): cost 0 </li>
     * </ul>
     */
    private static void initBaseMats() {
        baseMats = new MatrixGroup(2, KEYS.length(), KEYS.length());
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
    }

    /**
     * Put a single move to the correct position in baseMats.
     * It is called "safe" because it makes sure that the "to" key of the move is a valid key on keypad.
     * @param baseMats single move matrix group
     * @param from "from" key of the move
     * @param i "to" key's vertical index on keypad
     * @param j "to" key's horizontal index on keypad
     */
    private static void safePut(MatrixGroup baseMats, char from, int i, int j) {
        if (i >= 0 && i < 4 && j >= 0 && j < 5) {
            char to = KEYPAD[i][j];
            if (to != 'X') {
                int cost = VOWELS.contains(String.valueOf(to)) ? 1 : 0;
                baseMats.matAt(cost).set(KEYS.indexOf(from), KEYS.indexOf(to), 1);
            }
        }
    }

    /**
     * Initialize sumVector and reduceVector - both are row vectors for calculating sum of all elements in a matrix.
     * <ul>
     *     <li> sumVector: row vector of 1's. </li>
     *     <li> reduceVector: row vector of 1's except for the positions of vowels, at which the elements are 0's.
     *          This is for reducing from the total sum the number of sequences started from a vowel key. </li>
     * </ul>
     */
    private static void initSumAndReduceVectors() {
        sumVector = new SimpleMatrix(1, KEYS.length());
        reduceVector = new SimpleMatrix(1, KEYS.length());
        for (int i = 0; i < sumVector.numCols(); i++) {
            sumVector.set(0, i, 1);
            if (!VOWELS.contains(String.valueOf(KEYS.charAt(i))))
                reduceVector.set(0, i, 1);
        }
    }
}
