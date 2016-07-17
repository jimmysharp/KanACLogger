package jimmysharp.kanaclogger.model;

public class SubMap {
    private long id;
    private MapField mapField;
    private BattleType battleType;
    private int gp;
    private int seconds;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public MapField getMapField() {
        return mapField;
    }
    public void setMapField(MapField mapField) {
        this.mapField = mapField;
    }
    public BattleType getBattleType() {
        return battleType;
    }
    public void setBattleType(BattleType battleType) {
        this.battleType = battleType;
    }
    public int getGp() {
        return gp;
    }
    public void setGp(int gp) {
        this.gp = gp;
    }
    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
