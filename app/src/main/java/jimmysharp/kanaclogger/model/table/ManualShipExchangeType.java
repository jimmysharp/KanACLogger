package jimmysharp.kanaclogger.model.table;

public class ManualShipExchangeType {
    private final long id;
    private final String name;

    public ManualShipExchangeType(long id, String name) {
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
