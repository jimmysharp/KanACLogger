package jimmysharp.kanaclogger.model.table;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite2.BriteDatabase;

import java.util.List;

import io.reactivex.Observable;

public class MapAreaAccessor {
    private static final String TABLE_NAME = "MapArea";
    private static final String SQL_CREATE =
            "CREATE TABLE \"MapArea\" (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`name`\tTEXT NOT NULL,\n" +
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

    public static Observable<List<MapArea>> getAllMapAreasObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new MapArea(cursor.getLong(0),cursor.getString(1)));
    }
    public static List<MapArea> getAllMapAreas(BriteDatabase db){
        return getAllMapAreasObservable(db).blockingFirst(null);
    }
    public static Observable<MapArea> getMapAreaObservable(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new MapArea(cursor.getLong(0),cursor.getString(1)), null);
    }
    public static MapArea getMapArea(BriteDatabase db, long id){
        return getMapAreaObservable(db,id).blockingFirst(null);
    }
}
