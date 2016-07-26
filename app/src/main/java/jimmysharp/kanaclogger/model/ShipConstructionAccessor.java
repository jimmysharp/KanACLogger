package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

public class ShipConstructionAccessor {
    private static final String TABLE_NAME = "ShipConstruction";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ShipConstruction\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`shipTransaction`\tINTEGER NOT NULL UNIQUE,\n" +
                    "\t`fuel`\tINTEGER NOT NULL,\n" +
                    "\t`bullet`\tINTEGER NOT NULL,\n" +
                    "\t`steel`\tINTEGER NOT NULL,\n" +
                    "\t`bauxite`\tINTEGER NOT NULL\n" +
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

    public static Observable<List<ShipConstruction>> getAllShipConstructions(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new ShipConstruction(
                        cursor.getLong(0),
                        ShipTransactionAccessor.getShipTransaction(db,cursor.getLong(1)),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5)
                ));
    }
    public static Observable<ShipConstruction> getShipConstruction(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOne(cursor -> new ShipConstruction(
                        cursor.getLong(0),
                        ShipTransactionAccessor.getShipTransaction(db,cursor.getLong(1)),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5)
                ));
    }
}
