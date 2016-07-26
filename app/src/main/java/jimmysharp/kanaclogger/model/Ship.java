package jimmysharp.kanaclogger.model;

import rx.Observable;

public class Ship {
    private final long id;
    private final String name;
    private final String kana;
    private final Observable<ShipType> shipType;
    private final int remodelled;
    private final int sortId;
    private final int originId;

    public Ship(long id, String name, String kana, Observable<ShipType> shipType, int remodelled, int sortId, int originId) {
        this.id = id;
        this.name = name;
        this.kana = kana;
        this.shipType = shipType;
        this.remodelled = remodelled;
        this.sortId = sortId;
        this.originId = originId;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getKana() {
        return kana;
    }
    public Observable<ShipType> getShipType() {
        return shipType;
    }
    public int getRemodelled() {
        return remodelled;
    }
    public int getSortId() {
        return sortId;
    }
    public int getOriginId() {
        return originId;
    }
}
