package jimmysharp.kanaclogger.model;

public class MapField {
    private long id;
    private MapArea mapArea;
    private int number;
    private String idName;
    private String name;
    private String operationName;
    private String operationComment;
    private int level;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public MapArea getMapArea() {
        return mapArea;
    }
    public void setMapArea(MapArea mapArea) {
        this.mapArea = mapArea;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getIdName() {
        return idName;
    }
    public void setIdName(String idName) {
        this.idName = idName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public String getOperationComment() {
        return operationComment;
    }
    public void setOperationComment(String operationComment) {
        this.operationComment = operationComment;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
}
