package jimmysharp.kanaclogger.model;

public class Ship {
    private long id;
    private String name;
    private String kana;
    private ShipType shipType;
    private int remodelled;
    private int sortId;
    private int originId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKana() {
        return kana;
    }
    public void setKana(String kana) {
        this.kana = kana;
    }
    public ShipType getShipType() {
        return shipType;
    }
    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }
    public int getRemodelled() {
        return remodelled;
    }
    public void setRemodelled(int remodelled) {
        this.remodelled = remodelled;
    }
    public int getSortId() {
        return sortId;
    }
    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
    public int getOriginId() {
        return originId;
    }
    public void setOriginId(int originId) {
        this.originId = originId;
    }
}
