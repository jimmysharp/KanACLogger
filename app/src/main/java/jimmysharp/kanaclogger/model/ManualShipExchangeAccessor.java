package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;

public class ManualShipExchangeAccessor {
    private static final String TABLE_NAME = "ManualShipExchange";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ManualShipExchange\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`shipTransaction`\tINTEGER NOT NULL,\n" +
                    "\t`exchangeType`\tINTEGER NOT NULL\n" +
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

    public Observable<List<ManualShipExchange>> getAllManualShipExchanges(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor ->
                        new ManualShipExchange(
                                cursor.getLong(0),
                                ShipTransactionAccessor.getShipTransaction(db,cursor.getLong(1)),
                                ManualShipExchangeTypeAccessor.getManualShipExchangeType(db,cursor.getLong(2))
                        ));
    }
    public Observable<ManualShipExchange> getManualShipExchange(BriteDatabase db, int id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + "WHERE _id = "+id)
                .mapToOne(cursor ->
                        new ManualShipExchange(
                                cursor.getLong(0),
                                ShipTransactionAccessor.getShipTransaction(db,cursor.getLong(1)),
                                ManualShipExchangeTypeAccessor.getManualShipExchangeType(db,cursor.getLong(2))
                        ));
    }
}
