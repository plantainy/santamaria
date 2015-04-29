package george.knightmoves;

import org.junit.Test;

import static org.junit.Assert.*;

public class KnightMoveAlgoTests {
    @Test
    public void testCalcNumSequences() {
        assertEquals(1, KnightMoveAlgo.calcNumSequences(10, 2));
    }
}
