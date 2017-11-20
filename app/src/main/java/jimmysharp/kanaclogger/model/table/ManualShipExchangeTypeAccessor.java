package jimmysharp.kanaclogger.model.table;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite2.BriteDatabase;

import java.util.List;

import io.reactivex.Observable;

public class ManualShipExchangeTypeAccessor {
    private static final String TABLE_NAME = "ManualShipExchangeType";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ManualShipExchangeType\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`name`\tTEXT NOT NULL\n" +
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

    public static Observable<List<ManualShipExchangeType>> getAllManualShipExchangeTypesObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ManualShipExchangeType(cursor.getLong(0),cursor.getString(1)));
    }
    public static List<ManualShipExchangeType> getAllManualShipExchangeTypes(BriteDatabase db){
        return getAllManualShipExchangeTypesObservable(db).blockingFirst(null);
    }
    public static Observable<ManualShipExchangeType> getManualShipExchangeTypeObservable(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new ManualShipExchangeType(cursor.getLong(0),cursor.getString(1)),null);
    }
    public static ManualShipExchangeType getManualShipExchangeType(BriteDatabase db, long id){
        return getManualShipExchangeTypeObservable(db, id).blockingFirst(null);
    }
}
