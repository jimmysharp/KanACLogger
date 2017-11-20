package jimmysharp.kanaclogger.model.table;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite2.BriteDatabase;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import java.util.List;

import io.reactivex.Observable;

public class ShipTransactionAccessor {
    private static final String TABLE_NAME = "ShipTransaction";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ShipTransaction\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`date`\tINTEGER NOT NULL,\n" +
                    "\t`card`\tINTEGER NOT NULL,\n" +
                    "\t`quantity`\tINTEGER NOT NULL\n" +
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

    public static Observable<List<ShipTransaction>> getAllShipTransactionsObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ShipTransaction(
                        cursor.getLong(0),
                        ZonedDateTime.ofInstant(Instant.ofEpochMilli(cursor.getLong(1)), ZoneId.systemDefault()),
                        CardAccessor.getCardObservable(db,cursor.getLong(2)),
                        cursor.getInt(3)
                ));
    }
    public static List<ShipTransaction> getAllShipTransaction(BriteDatabase db){
        return getAllShipTransactionsObservable(db).blockingFirst(null);
    }
    public static Observable<ShipTransaction> getShipTransactionObservable(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new ShipTransaction(
                        cursor.getLong(0),
                        ZonedDateTime.ofInstant(Instant.ofEpochMilli(cursor.getLong(1)), ZoneId.systemDefault()),
                        CardAccessor.getCardObservable(db,cursor.getLong(2)),
                        cursor.getInt(3)
                ),null);
    }
    public static ShipTransaction getShipTransaction(BriteDatabase db, long id){
        return getShipTransactionObservable(db, id).blockingFirst(null);
    }

    public static long insert(BriteDatabase db, ZonedDateTime date, long cardId, long quantity){
        ContentValues contents = new ContentValues();
        contents.put("date",date.toInstant().toEpochMilli());
        contents.put("card",cardId);
        contents.put("quantity",quantity);

        long result = db.insert(TABLE_NAME, contents);
        if (result == -1) throw new SQLException("Failed to insert ShipTransaction data.");

        return result;
    }

    public static long insert(BriteDatabase db, long cardId, long quantity){
        return insert(db,ZonedDateTime.now(),cardId,quantity);
    }
}
