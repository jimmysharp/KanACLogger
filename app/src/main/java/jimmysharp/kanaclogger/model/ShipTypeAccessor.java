package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

public class ShipTypeAccessor {
    private static final String TABLE_NAME = "ShipType";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ShipType\" (\n" +
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

    public static Observable<List<ShipType>> getAllShipTypes(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ShipType(cursor.getLong(0),cursor.getString(1)));
    }
    public static Observable<ShipType> getShipType(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + "WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new ShipType(cursor.getLong(0),cursor.getString(1)), null);
    }
}
