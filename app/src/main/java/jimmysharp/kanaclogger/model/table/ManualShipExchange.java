package jimmysharp.kanaclogger.model.table;

import rx.Observable;

public class ManualShipExchange {
    private final long id;
    private final Observable<ShipTransaction> shipTransaction;
    private final Observable<ManualShipExchangeType> exchangeType;

    public ManualShipExchange(long id, Observable<ShipTransaction> shipTransaction, Observable<ManualShipExchangeType> exchangeType) {
        this.id = id;
        this.shipTransaction = shipTransaction;
        this.exchangeType = exchangeType;
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
    public Observable<ManualShipExchangeType> getExchangeTypeObservable() {
        return exchangeType;
    }
    public ManualShipExchangeType getExchangeType(){
        return exchangeType.toBlocking().firstOrDefault(null);
    }
}
