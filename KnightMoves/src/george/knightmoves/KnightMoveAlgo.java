package george.knightmoves;

import org.ejml.simple.SimpleMatrix;

public class KnightMoveAlgo {
    private static final int MAX_VOWELS = 2;
    public static long calcNumSequences(int length) {
        if (length < 1)
            return 0;
        if (length == 1)
            return KeypadInitializer.getNumKeys();
        String reverseBinaryStr = new StringBuilder(Integer.toBinaryString(length - 1)).reverse().toString();
        SimpleMatrix[] lastMats = getLastMats(KeypadInitializer.initBaseMats(), reverseBinaryStr);
        SimpleMatrix[] frontVectors = KeypadInitializer.initFrontVectors();
        SimpleMatrix[] finalVectors = multiplyMats(frontVectors, lastMats);
        SimpleMatrix sumVector = KeypadInitializer.initSumVector();
        long numSeq = 0;
        for (SimpleMatrix vector : finalVectors)
            numSeq += vector.mult(sumVector).get(0, 0);
        return numSeq;
    }

    private static SimpleMatrix[] getLastMats(SimpleMatrix[] mats, String reverseBinaryStr) {
        SimpleMatrix[] lastMats = null;
        for (int i = 0; i < reverseBinaryStr.length(); i++) {
            if (i != 0)
                mats = multiplyMats(mats, mats);
            if (reverseBinaryStr.charAt(i) == '1')
                lastMats = lastMats == null ? mats : multiplyMats(lastMats, mats);
        }
        return lastMats;
    }

    private static SimpleMatrix[] multiplyMats(SimpleMatrix[] leftMats, SimpleMatrix[] rightMats) {
        SimpleMatrix[] resMats = new SimpleMatrix[MAX_VOWELS + 1];
        for (int i = 0; i < resMats.length; i++)
            resMats[i] = new SimpleMatrix(leftMats[0].numRows(), rightMats[0].numCols());
        for (int i = 0; i < leftMats.length; i++)
            for (int j = 0; j < rightMats.length; j++)
                if (i + j <= MAX_VOWELS)
                    resMats[i + j] = resMats[i + j].plus(leftMats[i].mult(rightMats[j]));
        return resMats;
    }
}
