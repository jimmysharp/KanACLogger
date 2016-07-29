package jimmysharp.kanaclogger.model.table;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import java.util.List;
import rx.Observable;

public class ShipTransactionAccessor {
    private static final String TABLE_NAME = "ShipTransaction";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ShipTransaction\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`date`\tINTEGER NOT NULL,\n" +
                    "\t`ship`\tINTEGER NOT NULL,\n" +
                    "\t`cardType`\tINTEGER NOT NULL,\n" +
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

    public static Observable<List<ShipTransaction>> getShipTransactions(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ShipTransaction(
                        cursor.getLong(0),
                        ZonedDateTime.ofInstant(Instant.ofEpochMilli(cursor.getLong(1)), ZoneId.systemDefault()),
                        ShipAccessor.getShip(db,cursor.getLong(2)),
                        CardTypeAccessor.getCardType(db,cursor.getLong(3)),
                        cursor.getInt(4)
                ));
    }
    public static Observable<ShipTransaction> getShipTransaction(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOne(cursor -> new ShipTransaction(
                        cursor.getLong(0),
                        ZonedDateTime.ofInstant(Instant.ofEpochMilli(cursor.getLong(1)), ZoneId.systemDefault()),
                        ShipAccessor.getShip(db,cursor.getLong(2)),
                        CardTypeAccessor.getCardType(db,cursor.getLong(3)),
                        cursor.getInt(4)
                ));
    }

    public static long insert(BriteDatabase db, ZonedDateTime date, long shipId, long cardTypeId, long quantity){
        ContentValues contents = new ContentValues();
        contents.put("date",date.toInstant().toEpochMilli());
        contents.put("ship",shipId);
        contents.put("cardType",cardTypeId);
        contents.put("quantity",quantity);

        long result = db.insert(TABLE_NAME, contents);
        if (result == -1) throw new SQLException("Failed to insert ShipTransaction data.");

        return result;
    }
}
