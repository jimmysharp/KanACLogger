package jimmysharp.kanaclogger.model.table;

import rx.Observable;

public class SubMap {
    private final long id;
    private final Observable<MapField> mapField;
    private final Observable<BattleType> battleType;
    private final int gp;
    private final int seconds;

    public SubMap(long id, Observable<MapField> mapField, Observable<BattleType> battleType, int gp, int seconds) {
        this.id = id;
        this.mapField = mapField;
        this.battleType = battleType;
        this.gp = gp;
        this.seconds = seconds;
    }

    public long getId() {
        return id;
    }
    public Observable<MapField> getMapFieldObservable() {
        return mapField;
    }
    public MapField getMapField(){
        return mapField.toBlocking().firstOrDefault(null);
    }
    public Observable<BattleType> getBattleTypeObservable() {
        return battleType;
    }
    public BattleType getBattleType(){
        return battleType.toBlocking().firstOrDefault(null);
    }
    public int getGp() {
        return gp;
    }
    public int getSeconds() {
        return seconds;
    }
}
