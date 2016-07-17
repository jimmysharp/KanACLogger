package jimmysharp.kanaclogger.model;


public class ManualShipExchange {
    private long id;
    private ShipTransaction shipTransaction;
    private ManualShipExchangeType type;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public ShipTransaction getShipTransaction() {
        return shipTransaction;
    }
    public void setShipTransaction(ShipTransaction shipTransaction) {
        this.shipTransaction = shipTransaction;
    }
    public ManualShipExchangeType getType() {
        return type;
    }
    public void setType(ManualShipExchangeType type) {
        this.type = type;
    }
}
