package jimmysharp.kanaclogger.model;

public class ShipDrop {
    private long id;
    private ShipTransaction shipTransaction;
    private SubMap subMap;

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
    public SubMap getSubMap() {
        return subMap;
    }
    public void setSubMap(SubMap subMap) {
        this.subMap = subMap;
    }
}
