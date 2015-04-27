package george.knightmoves;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GroupedKeyList {
    private final List<List<Character>> groups;

    public GroupedKeyList(int numGroups) {
        this.groups = new ArrayList<>(numGroups);
        for (int i = 0; i < numGroups; i++)
            groups.add(new LinkedList<>());
    }

    public List<Character> getGroup(int cost) {
        return groups.get(cost);
    }

    public void addKeyToGroup(char key, int cost) {
        groups.get(cost).add(key);
    }

    public void addAllKeysToGroup(List<Character> ends, int cost) {
        groups.get(cost).addAll(ends);
    }

    public int getNumOfGroups() {
        return groups.size();
    }

    public int getNumAllKeys() {
        int sum = 0;
        for (List<Character> group : groups)
            sum += group.size();
        return sum;
    }
}
