package jimmysharp.kanaclogger.model;

public class MapArea {
    private final long id;
    private final String name;

    public MapArea(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
