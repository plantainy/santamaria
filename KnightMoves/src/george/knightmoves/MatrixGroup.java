package george.knightmoves;

import org.ejml.simple.SimpleMatrix;

/**
 * MatrixGroup is a wrapper of a matrix array, which holds matrices of the same size.
 * It is constructed with a number of zero matrices,
 * and it publishes some common properties (numRows and numCols) of its composite matrices.
 */
public class MatrixGroup {
    private final SimpleMatrix[] group;
    private final int matNumRows;
    private final int matNumCols;

    /**
     * Construct of a group of zero matrices of the same size.
     * @param size size of the group.
     * @param matNumRows number of rows of a composite matrix.
     * @param matNumCols number of columns of a composite matrix.
     */
    public MatrixGroup(int size, int matNumRows, int matNumCols) {
        this.group = new SimpleMatrix[size];
        this.matNumRows = matNumRows;
        this.matNumCols = matNumCols;
        for (int i = 0; i < size; i++)
            group[i] = new SimpleMatrix(matNumRows, matNumCols);
    }

    public SimpleMatrix matAt(int index) {
        return group[index];
    }

    public void setMat(SimpleMatrix mat, int index) {
        group[index] = mat;
    }

    public int getMatNumRows() {
        return matNumRows;
    }

    public int getMatNumCols() {
        return matNumCols;
    }

    public int size() {
        return group.length;
    }
}
