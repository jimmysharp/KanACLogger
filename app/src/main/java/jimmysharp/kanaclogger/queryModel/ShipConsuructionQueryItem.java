package jimmysharp.kanaclogger.queryModel;

import jimmysharp.kanaclogger.model.ShipTransaction;

public class ShipConstructionQueryItem {
    private Long id;
    private ShipTransaction shipTransaction;
    private int fuel;
    private int bullet;
    private int steel;
    private int bauxite;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ShipTransaction getShipTransaction() {
        return shipTransaction;
    }
    public void setShipTransaction(ShipTransaction shipTransaction) {
        this.shipTransaction = shipTransaction;
    }
    public int getFuel() {
        return fuel;
    }
    public void setFuel(int fuel) {
        this.fuel = fuel;
    }
    public int getBullet() {
        return bullet;
    }
    public void setBullet(int bullet) {
        this.bullet = bullet;
    }
    public int getSteel() {
        return steel;
    }
    public void setSteel(int steel) {
        this.steel = steel;
    }
    public int getBauxite() {
        return bauxite;
    }
    public void setBauxite(int bauxite) {
        this.bauxite = bauxite;
    }
}
