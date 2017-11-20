package jimmysharp.kanaclogger.model.table;

import io.reactivex.Observable;

public class ShipConstruction {
    private final long id;
    private final Observable<ShipTransaction> shipTransaction;
    private final int fuel;
    private final int bullet;
    private final int steel;
    private final int bauxite;

    public ShipConstruction(long id, Observable<ShipTransaction> shipTransaction, int fuel, int bullet, int steel, int bauxite) {
        this.id = id;
        this.shipTransaction = shipTransaction;
        this.fuel = fuel;
        this.bullet = bullet;
        this.steel = steel;
        this.bauxite = bauxite;
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
    public int getFuel() {
        return fuel;
    }
    public int getBullet() {
        return bullet;
    }
    public int getSteel() {
        return steel;
    }
    public int getBauxite() {
        return bauxite;
    }
}
