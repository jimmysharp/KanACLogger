package jimmysharp.kanaclogger.model.table;

import android.content.ContentValues;
import android.database.SQLException;
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

    public static Observable<List<ShipDrop>> getAllShipDropsObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ShipDrop(
                cursor.getLong(0),
                ShipTransactionAccessor.getShipTransactionObservable(db,cursor.getLong(1)),
                SubMapAccessor.getSubMapObservable(db,cursor.getLong(2))
        ));
    }
    public static List<ShipDrop> getAllShipDrops(BriteDatabase db){
        return getAllShipDropsObservable(db).toBlocking().firstOrDefault(null);
    }
    public static Observable<ShipDrop> getShipDropObservable(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new ShipDrop(
                        cursor.getLong(0),
                        ShipTransactionAccessor.getShipTransactionObservable(db,cursor.getLong(1)),
                        SubMapAccessor.getSubMapObservable(db,cursor.getLong(2))
                ),null);
    }
    public static ShipDrop getShipDrop(BriteDatabase db, long id){
        return getShipDropObservable(db,id).toBlocking().firstOrDefault(null);
    }

    public static long insert(BriteDatabase db, long shipTransactionId, long subMapId){
        ContentValues contents = new ContentValues();
        contents.put("shipTransaction",shipTransactionId);
        contents.put("subMap",subMapId);

        long result = db.insert(TABLE_NAME, contents);
        if (result == -1) throw new SQLException("Failed to insert ShipDrop data.");

        return result;
    }
}
