package george.knightmoves;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempAlgo {
    private static final int MAX_VOWELS = 2;

    public static int calcNumSequences(int length) {
        if (length < 1)
            return 0;
        String lenBinaryStr = Integer.toBinaryString(length);
        List<Map<Character, GroupedKeyList>> snapshots = new ArrayList<>();
        buildMoves(snapshots, MoveMapHolder.getMoveMap(), lenBinaryStr, 0);
        GroupedKeyList endList = snapshots.get(0).get('X');
        for (int i = 1; i < snapshots.size(); i++) {
            endList = getNextList(endList, snapshots.get(i));
        }
        return endList.getNumOfAllKeys();
    }

    private static void buildMoves(List<Map<Character, GroupedKeyList>> snapshots,
                                   Map<Character, GroupedKeyList> moveMap,
                                   String lenBinaryStr, int index) {
        if (index == lenBinaryStr.length())
            return;
        Map<Character, GroupedKeyList> doubledMoveMap = new HashMap<>(moveMap);
        if (index != 0) {
            for (char key : moveMap.keySet()) {
                if (key == 'X' && !snapshots.isEmpty())
                    continue;
                doubledMoveMap.put(key, getNextList(moveMap.get(key), moveMap));
            }
        }
        if (lenBinaryStr.charAt(lenBinaryStr.length() - index - 1) == '1')
            snapshots.add(doubledMoveMap);
        buildMoves(snapshots, doubledMoveMap, lenBinaryStr, index + 1);
    }

    private static GroupedKeyList getNextList(GroupedKeyList startList, Map<Character, GroupedKeyList> moveMap) {
        GroupedKeyList nextList = new GroupedKeyList(MAX_VOWELS + 1);
        for (int i = 0; i < startList.getNumOfGroups(); i++) {
            String group = startList.getGroup(i);
            for (char start : group.toCharArray()) {
                GroupedKeyList moveList = moveMap.get(start);
                for (int j = 0; j < moveList.getNumOfGroups(); j++) {
                    if (i + j <= MAX_VOWELS)
                        nextList.addAllKeysToGroup(moveList.getGroup(j), i + j);
                }
            }
        }
        return nextList;
    }
}
