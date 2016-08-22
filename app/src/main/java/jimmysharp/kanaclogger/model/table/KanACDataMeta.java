package jimmysharp.kanaclogger.model.table;

public class KanACDataMeta {
    private final String key;
    private final int value;

    public KanACDataMeta(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public int getValue() {
        return value;
    }
}
