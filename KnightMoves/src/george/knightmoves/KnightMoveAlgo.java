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
     *     <li> Calculate the final matrix group after all moves from keys on keypad.
     *          number of moves = path lengh - 1 </li>
     *     <li> As baseMats from KeypadInitializer doesn't consider the cost of a single move from a vowel,
     *          the number of sequences started from a vowel can be too big.
     *          This is, in the last matrix of the final matrix group,
     *          all rows corresponding to vowels should be dropped, as they actually carry more cost than limit.
     *          Therefore, the result is the sum of all elements in the final matrix group except for those dropped.
     *          This is done by applying reduceVector to the last matrix.
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
        MatrixGroup finalMats = getFinalMats(groupSize, KeypadInitializer.getBaseMats(), movesInBinary);
        SimpleMatrix sumVector = KeypadInitializer.getSumVector();
        SimpleMatrix reduceVector = KeypadInitializer.getReduceVector();
        long numSeq = 0;
        for (int i = 0; i < finalMats.size() - 1; i++)
            numSeq += sumVector.mult(finalMats.matAt(i)).mult(sumVector.transpose()).get(0, 0);
        numSeq += reduceVector.mult(finalMats.matAt(finalMats.size() - 1)).mult(sumVector.transpose()).get(0, 0);
        return numSeq;
    }

    /**
     * Returns the final matrix group after all the moves.
     * The complexity is O(log n) where n = numMoves, as the calculation is on binary sequence. <br/>
     * @param groupSize size of the last matrix group
     * @param mats base mats for single move
     * @param movesInBinary the number of moves in binary string (reversed), used to optimize the procedure.
     * @return the final matrix group after all moves
     */
    private static MatrixGroup getFinalMats(int groupSize, MatrixGroup mats, String movesInBinary) {
        MatrixGroup finalMats = null;
        for (int i = 0; i < movesInBinary.length(); i++) {
            if (i != 0)
                mats = combineMatGroups(groupSize, mats, mats);
            if (movesInBinary.charAt(i) == '1')
                finalMats = finalMats == null ? mats : combineMatGroups(groupSize, finalMats, mats);
        }
        return finalMats;
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
