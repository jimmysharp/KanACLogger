package jimmysharp.kanaclogger.model.table;

import rx.Observable;

public class MapField {
    private final long id;
    private final Observable<MapArea> mapArea;
    private final int number;
    private final String idName;
    private final String name;
    private final String operationName;
    private final String operationComment;
    private final int level;

    public MapField(long id, Observable<MapArea> mapArea, int number, String idName, String name, String operationName, String operationComment, int level) {
        this.id = id;
        this.mapArea = mapArea;
        this.number = number;
        this.idName = idName;
        this.name = name;
        this.operationName = operationName;
        this.operationComment = operationComment;
        this.level = level;
    }

    public long getId() {
        return id;
    }
    public Observable<MapArea> getMapArea() {
        return mapArea;
    }
    public int getNumber() {
        return number;
    }
    public String getIdName() {
        return idName;
    }
    public String getName() {
        return name;
    }
    public String getOperationName() {
        return operationName;
    }
    public String getOperationComment() {
        return operationComment;
    }
    public int getLevel() {
        return level;
    }
}
