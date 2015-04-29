package george.knightmoves;

public class KnightMoveAlgoTestRunner {
    public static void main(String[] args) {
        int[] lengths = {10, 16, 32};
        int maxVowels = 2;
        for (int length : lengths)
            System.out.println(String.format("Length %d maxVowels %d: %d",
                    length, maxVowels, KnightMoveAlgo.calcNumSequences(length, maxVowels)));
    }
}
