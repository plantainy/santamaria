package george.knightmoves;

import java.util.List;
import java.util.Map;

public class KnightMoveAlgo {
    private static final int MAX_VOWELS = 2;
    private static final Map<Character, GroupedKeyList> moveMap = KeypadHolder.getMoveMap();
    private static final Map<Character, GroupedKeyList> reducedMoveMap = KeypadHolder.getReducedMoveMap();

    public static int calcNumSequences(int n) {
        if (n < 1)
            return 0;
        if (n == 1)
            return moveMap.size();
        int numSeq = 0;
        for (GroupedKeyList groups : moveMap.values())
            numSeq += getEndKeyList(groups, n).getNumAllKeys();
        return numSeq;
    }

    private static GroupedKeyList getEndKeyList(GroupedKeyList startList, int n) {
        if (n == 2)
            return startList;
        GroupedKeyList endList = new GroupedKeyList(MAX_VOWELS + 1);
        for (int i = 0; i < startList.getNumOfGroups(); i++) {
            List<Character> group = startList.getGroup(i);
            for (char start : group) {
                GroupedKeyList nextList = reducedMoveMap.get(start);
                for (int j = 0; j < nextList.getNumOfGroups(); j++)
                    if (i + j <= MAX_VOWELS)
                        endList.addAllKeysToGroup(nextList.getGroup(j), i + j);
            }
        }
        return getEndKeyList(endList, n - 1);
    }
}
