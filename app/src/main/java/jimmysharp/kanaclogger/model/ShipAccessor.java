package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

public class ShipAccessor {
    private static final String TABLE_NAME = "Ship";
    private static final String SQL_CREATE =
            "CREATE TABLE \"Ship\" (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`name`\tTEXT NOT NULL,\n" +
                    "\t`kana`\tTEXT NOT NULL,\n" +
                    "\t`shipType`\tINTEGER NOT NULL,\n" +
                    "\t`remodelled`\tINTEGER NOT NULL,\n" +
                    "\t`sortId`\tINTEGER NOT NULL,\n" +
                    "\t`originId`\tINTEGER NOT NULL,\n" +
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

    public static Observable<List<Ship>> getAllShips(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new Ship(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ShipTypeAccessor.getShipType(db,cursor.getLong(3)),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
    }
    public static Observable<Ship> getShip(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOne(cursor -> new Ship(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ShipTypeAccessor.getShipType(db,cursor.getLong(3)),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
    }
}
