package jimmysharp.kanaclogger.model.table;

import rx.Observable;

public class ShipDrop {
    private final long id;
    private final Observable<ShipTransaction> shipTransaction;
    private final Observable<SubMap> subMap;

    public ShipDrop(long id, Observable<ShipTransaction> shipTransaction, Observable<SubMap> subMap) {
        this.id = id;
        this.shipTransaction = shipTransaction;
        this.subMap = subMap;
    }

    public long getId() {
        return id;
    }
    public Observable<ShipTransaction> getShipTransactionObservable() {
        return shipTransaction;
    }
    public ShipTransaction getShipTransaction(){
        return shipTransaction.toBlocking().firstOrDefault(null);
    }
    public Observable<SubMap> getSubMapObservable() {
        return subMap;
    }
    public SubMap getSubMap(){
        return subMap.toBlocking().firstOrDefault(null);
    }
}
