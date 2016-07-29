package jimmysharp.kanaclogger.model.table;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

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

    public static Observable<List<ManualShipExchangeType>> getAllManualShipExchangeTypes(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ManualShipExchangeType(cursor.getLong(0),cursor.getString(1)));
    }
    public static Observable<ManualShipExchangeType> getManualShipExchangeType(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOne(cursor -> new ManualShipExchangeType(cursor.getLong(0),cursor.getString(1)));
    }
}
