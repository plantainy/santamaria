package george.knightmoves;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class KeypadHolderTests {
    @Test
    public void testMoveMapBuilder() {
        Map<Character, GroupedKeyList> moveMap = KeypadHolder.getMoveMap();

        assertEquals(2, moveMap.get('A').getNumAllKeys());
        assertTrue(moveMap.get('A').getGroup(0).isEmpty());
        assertTrue(moveMap.get('A').getGroup(1).contains('H'));

        assertEquals(6, moveMap.get('H').getNumAllKeys());
        assertTrue(moveMap.get('H').getGroup(0).contains('1'));
        assertTrue(moveMap.get('H').getGroup(1).contains('A'));

        assertEquals(3, moveMap.get('3').getNumAllKeys());
        assertTrue(moveMap.get('3').getGroup(0).contains('J'));
        assertTrue(moveMap.get('3').getGroup(1).isEmpty());
    }

    @Test
    public void testReducedMoveMapBuilder() {
        Map<Character, GroupedKeyList> moveMap = KeypadHolder.getReducedMoveMap();

        assertEquals(2, moveMap.get('A').getNumAllKeys());
        assertTrue(moveMap.get('A').getGroup(0).contains('H'));
        assertTrue(moveMap.get('A').getGroup(1).isEmpty());

        assertEquals(6, moveMap.get('H').getNumAllKeys());
        assertTrue(moveMap.get('H').getGroup(0).contains('1'));
        assertTrue(moveMap.get('H').getGroup(1).contains('A'));

        assertEquals(3, moveMap.get('3').getNumAllKeys());
        assertTrue(moveMap.get('3').getGroup(0).contains('J'));
        assertTrue(moveMap.get('3').getGroup(1).isEmpty());
    }
}
