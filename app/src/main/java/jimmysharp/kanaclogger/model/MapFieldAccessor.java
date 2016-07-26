package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

public class MapFieldAccessor {
    private static final String TABLE_NAME = "MapField";
    private static final String SQL_CREATE =
            "CREATE TABLE \"MapField\" (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`mapArea`\tINTEGER NOT NULL,\n" +
                    "\t`number`\tINTEGER NOT NULL,\n" +
                    "\t`idName`\tTEXT NOT NULL,\n" +
                    "\t`name`\tTEXT NOT NULL,\n" +
                    "\t`operationName`\tTEXT NOT NULL,\n" +
                    "\t`operationComment`\tTEXT NOT NULL,\n" +
                    "\t`level`\tINTEGER NOT NULL,\n" +
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

    public static Observable<List<MapField>> getAllMapFields(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new MapField(
                        cursor.getLong(0),
                        MapAreaAccessor.getMapArea(db,cursor.getLong(1)),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                ));
    }
    public static Observable<MapField> getMapField(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME +
                " WHERE _id = "+id)
                .mapToOne(cursor -> new MapField(
                        cursor.getLong(0),
                        MapAreaAccessor.getMapArea(db,cursor.getLong(1)),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                ));
    }
}
