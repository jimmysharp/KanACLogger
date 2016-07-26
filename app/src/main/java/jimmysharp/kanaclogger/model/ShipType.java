package jimmysharp.kanaclogger.model;

public class ShipType {
    private final long id;
    private final String name;

    public ShipType(long id, String name) {
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
