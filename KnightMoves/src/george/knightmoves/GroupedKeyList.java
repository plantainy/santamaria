package george.knightmoves;

public class GroupedKeyList {
    private final String[] nexts;

    public GroupedKeyList(int numGroups) {
        this.nexts = new String[numGroups];
        for (int i = 0; i < numGroups; i++)
            nexts[i] = "".intern();
    }

    public String getGroup(int cost) {
        return nexts[cost];
    }

    public void addKeyToGroup(char key, int cost) {
        nexts[cost] += key;
    }

    public void addAllKeysToGroup(String ends, int cost) {
        nexts[cost] += ends;
    }

    public int getNumOfGroups() {
        return nexts.length;
    }

    public int getNumOfAllKeys() {
        int sum = 0;
        for (String group : nexts)
            sum += group.length();
        return sum;
    }
}
