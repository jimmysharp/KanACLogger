package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

public class ShipDropAccessor {
    private static final String TABLE_NAME = "ShipDrop";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ShipDrop\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`shipTransaction`\tINTEGER NOT NULL UNIQUE,\n" +
                    "\t`subMap`\tINTEGER NOT NULL\n" +
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

    public static Observable<List<ShipDrop>> getAllShipDrops(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ShipDrop(
                cursor.getLong(0),
                ShipTransactionAccessor.getShipTransaction(db,cursor.getLong(1)),
                SubMapAccessor.getSubMap(db,cursor.getLong(2))
        ));
    }
    public static Observable<ShipDrop> getShipDrop(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOne(cursor -> new ShipDrop(
                        cursor.getLong(0),
                        ShipTransactionAccessor.getShipTransaction(db,cursor.getLong(1)),
                        SubMapAccessor.getSubMap(db,cursor.getLong(2))
                ));
    }
}
