package jimmysharp.kanaclogger.model.table;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;

public class SubMapAccessor {
    private static final String TABLE_NAME = "SubMap";
    private static final String SQL_CREATE =
            "CREATE TABLE \"SubMap\" (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`mapField`\tINTEGER NOT NULL,\n" +
                    "\t`battleType`\tINTEGER NOT NULL,\n" +
                    "\t`gp`\tINTEGER NOT NULL,\n" +
                    "\t`seconds`\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(_id)\n" +
                    ")";

    public static void create(SQLiteDatabase db){
        db.execSQL(SQL_CREATE);
    }
    public static String getCreateSQL(){
        return SQL_CREATE;
    }
    public static String getTableName() {
        return TABLE_NAME;
    }

    public static Observable<List<SubMap>> getAllSubMapsObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new SubMap(
                        cursor.getLong(0),
                        MapFieldAccessor.getMapFieldObservable(db,cursor.getLong(1)),
                        BattleTypeAccessor.getBattleTypeObservable(db,cursor.getLong(2)),
                        cursor.getInt(3),
                        cursor.getInt(4)
                ));
    }
    public static List<SubMap> getAllSubMaps(BriteDatabase db){
        return getAllSubMapsObservable(db).toBlocking().firstOrDefault(null);
    }
    public static Observable<SubMap> getSubMapObservable(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOne(cursor -> new SubMap(
                        cursor.getLong(0),
                        MapFieldAccessor.getMapFieldObservable(db,cursor.getLong(1)),
                        BattleTypeAccessor.getBattleTypeObservable(db,cursor.getLong(2)),
                        cursor.getInt(3),
                        cursor.getInt(4)
                ));
    }
    public static SubMap getSubMap(BriteDatabase db, long id){
        return getSubMapObservable(db, id).toBlocking().firstOrDefault(null);
    }

    public static Observable<SubMap> getSubMapObservable(BriteDatabase db, long mapFieldId, long battleTypeId){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE mapField = " + mapFieldId
                + " AND battleType = " + battleTypeId)
                .mapToOneOrDefault(cursor -> new SubMap(
                        cursor.getLong(0),
                        MapFieldAccessor.getMapFieldObservable(db,cursor.getLong(1)),
                        BattleTypeAccessor.getBattleTypeObservable(db,cursor.getLong(2)),
                        cursor.getInt(3),
                        cursor.getInt(4)
                ),null);
    }
    public static SubMap getSubMap(BriteDatabase db, long mapFieldId, long battleTypeId){
        return getSubMapObservable(db, mapFieldId, battleTypeId).toBlocking().firstOrDefault(null);
    }
}
