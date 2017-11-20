package jimmysharp.kanaclogger.model.table;

import io.reactivex.Observable;

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
        return shipTransaction.blockingFirst(null);
    }
    public Observable<ManualShipExchangeType> getExchangeTypeObservable() {
        return exchangeType;
    }
    public ManualShipExchangeType getExchangeType(){
        return exchangeType.blockingFirst(null);
    }
}
