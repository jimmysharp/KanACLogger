package jimmysharp.kanaclogger.model.table;


import rx.Observable;

public class ManualShipExchange {
    private final long id;
    private final Observable<ShipTransaction> shipTransaction;
    private final Observable<ManualShipExchangeType> type;

    public ManualShipExchange(long id, Observable<ShipTransaction> shipTransaction, Observable<ManualShipExchangeType> type) {
        this.id = id;
        this.shipTransaction = shipTransaction;
        this.type = type;
    }

    public long getId() {
        return id;
    }
    public Observable<ShipTransaction> getShipTransaction() {
        return shipTransaction;
    }
    public Observable<ManualShipExchangeType> getType() {
        return type;
    }
}
