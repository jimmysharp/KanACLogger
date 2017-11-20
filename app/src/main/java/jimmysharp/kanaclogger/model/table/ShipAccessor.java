package jimmysharp.kanaclogger.model.table;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite2.BriteDatabase;

import java.util.List;

import io.reactivex.Observable;

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

    public static Observable<List<Ship>> getAllShipsObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new Ship(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ShipTypeAccessor.getShipTypeObservable(db,cursor.getLong(3)),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
    }
    public static List<Ship> getAllShips(BriteDatabase db){
        return getAllShipsObservable(db).blockingFirst(null);
    }

    public static Observable<List<Ship>> getAllShipsSortedObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME + " ORDER BY `sortId`, `remodelled`")
                .mapToList(cursor -> new Ship(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ShipTypeAccessor.getShipTypeObservable(db,cursor.getLong(3)),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
    }
    public static List<Ship> getAllShipsSorted(BriteDatabase db){
        return getAllShipsSortedObservable(db).blockingFirst(null);
    }

    public static Observable<List<Ship>> getShipsObservable(BriteDatabase db, ShipType shipType, Boolean remodelled){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1 = 1";
        if (shipType != null) query += " AND shipType = "+shipType.getId();
        if (remodelled != null){
            if (remodelled) query += " AND remodelled > 0";
            else query += " AND remodelled = 0";
        }
        query += " ORDER BY `sortId`, `remodelled`";

        return db.createQuery(TABLE_NAME, query)
                .mapToList(cursor -> new Ship(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ShipTypeAccessor.getShipTypeObservable(db,cursor.getLong(3)),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
    }
    public static List<Ship> getShips(BriteDatabase db, ShipType shipType, Boolean remodelled){
        return getShipsObservable(db, shipType, remodelled).blockingFirst(null);
    }

    public static Observable<Ship> getShipObservable(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new Ship(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ShipTypeAccessor.getShipTypeObservable(db,cursor.getLong(3)),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ),null);
    }
    public static Ship getShip(BriteDatabase db, long id){
        return getShipObservable(db,id).blockingFirst(null);
    }
}
