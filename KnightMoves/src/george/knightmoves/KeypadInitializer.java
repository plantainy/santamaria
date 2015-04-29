package george.knightmoves;

import org.ejml.simple.SimpleMatrix;

/**
 * The Knight move algorithm class.
 */
public class KnightMoveAlgo {

    /**
     * The algorithm to calculate number of sequences given the path length and max number of vowels on a path.
     * An illustration of the algorithm:
     * <ul>
     *     <li> Calculate the last matrix group after all moves from keys on keypad.
     *          number of moves = path lengh - 1 </li>
     *     <li> As baseMats from KeypadInitializer doesn't count the cost of a move starting from a vowel,
     *          the number of sequences from a vowel can be too big thus needs to be reduced.
     *          This is done by assuming that there is a invisible starting key,
     *          which has a single move to each key on keypad, and such move costs 1 if the next key is a vowel.
     *          This single move is captured by frontVectors,
     *          which is applied to the last matrix group to calculate all sequences from the starting key.
     *          This reduces the over-counting of the sequences from vowel keys
     *          and results a group of row vectors called finalVectors. </li>
     *     <li> Finally, sum all numbers in finalVectors by applying sumVector. </li>
     *
     * </ul>
     * @param length path length
     * @param maxVowels max number of vowels on a path
     * @return the total number of possible sequences
     */
    public static long calcNumSequences(int length, int maxVowels) {
        if (length < 1)
            return 0;
        if (length == 1)
            return KeypadInitializer.getNumKeys();
        int numMoves = length - 1;
        String movesInBinary = new StringBuilder(Integer.toBinaryString(numMoves)).reverse().toString();
        int groupSize = maxVowels + 1;
        MatrixGroup lastMats = getLastMats(groupSize, KeypadInitializer.initBaseMats(), movesInBinary);
        MatrixGroup frontVectors = KeypadInitializer.initFrontVectors();
        MatrixGroup finalVectors = combineMatGroups(groupSize, frontVectors, lastMats);
        SimpleMatrix sumVector = KeypadInitializer.initSumVector();
        long numSeq = 0;
        for (int i = 0; i < finalVectors.size(); i++)
            numSeq += finalVectors.matAt(i).mult(sumVector).get(0, 0);
        return numSeq;
    }

    /**
     * Returns the last matrix group after all the moves.
     * The complexity is O(log n) where n = numMoves, as the calculation is on binary sequence. <br/>
     * The last matrix group should be further reduced with a move from an invisible starting key to the keys on keypad.
     * @param groupSize size of the last matrix group
     * @param mats base mats for single move
     * @param movesInBinary the number of moves in binary string (reversed), used to optimize the procedure.
     * @return the last matrix group after all moves
     */
    private static MatrixGroup getLastMats(int groupSize, MatrixGroup mats, String movesInBinary) {
        MatrixGroup lastMats = null;
        for (int i = 0; i < movesInBinary.length(); i++) {
            if (i != 0)
                mats = combineMatGroups(groupSize, mats, mats);
            if (movesInBinary.charAt(i) == '1')
                lastMats = lastMats == null ? mats : combineMatGroups(groupSize, lastMats, mats);
        }
        return lastMats;
    }

    /**
     * Combine two matrix groups to a new matrix group of a fixed size. An example to illustrate the procedure:
     * <ul>
     *     <li> groupSize = 3, leftMats = {L0, L1, L2} and rightMats = {R0, R1, R2} 
     *          where L's and R's are move matrices and their positions in the group indicate the costs of the moves. </li>
     *     <li> The resulting combined matrix group will be {L0*R0, L0*R1+L1*R0, L0*L2+L1*R1+L2*R0}.
     *          Thus the positions of the matrices continue to indicate the costs of the moves. </li>
     *     <li> Other combinations, e.g., L1*R2, are dropped from the combined matrix group
     *          because the costs of the moves, e.g., 3 for L1*R2, exceed the limit. </li>
     * </ul>
     * @param groupSize the size of the combined matrix group.
     * @param leftMats first input matrix
     * @param rightMats second input matrix
     * @return a new matrix group as a result of combining leftMats and rightMats.
     */
    private static MatrixGroup combineMatGroups(int groupSize, MatrixGroup leftMats, MatrixGroup rightMats) {
        MatrixGroup combinedMats = new MatrixGroup(groupSize, leftMats.getMatNumRows(), rightMats.getMatNumCols());
        for (int i = 0; i < leftMats.size(); i++)
            for (int j = 0; j < rightMats.size(); j++)
                if (i + j < combinedMats.size()) {
                    SimpleMatrix combinedMat = combinedMats.matAt(i + j).plus(leftMats.matAt(i).mult(rightMats.matAt(j)));
                    combinedMats.setMat(combinedMat, i + j);
                }
        return combinedMats;
    }
}
