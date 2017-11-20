package jimmysharp.kanaclogger.model.table;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite2.BriteDatabase;

import java.util.List;

import io.reactivex.Observable;

public class CardAccessor {
    private static final String TABLE_NAME = "Card";
    public static final String SQL_CREATE =
            "CREATE TABLE \"Card\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY,\n" +
                    "\t`ship`\tINTEGER NOT NULL,\n" +
                    "\t`cardType`\tINTEGER NOT NULL\n" +
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

    public static Observable<List<Card>> getAllCardsObservable(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new Card(
                        cursor.getLong(0),
                        ShipAccessor.getShipObservable(db,cursor.getLong(2)),
                        CardTypeAccessor.getCardTypeObservable(db,cursor.getLong(3))
                ));
    }
    public static List<Card> getAllCards(BriteDatabase db){
        return getAllCardsObservable(db).blockingFirst(null);
    }
    public static Observable<Card> getCardObservable(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new Card(
                        cursor.getLong(0),
                        ShipAccessor.getShipObservable(db,cursor.getLong(1)),
                        CardTypeAccessor.getCardTypeObservable(db,cursor.getLong(2))
                ),null);
    }

    public static Card getCard(BriteDatabase db, long id){
        return getCardObservable(db,id).blockingFirst(null);
    }

    public static Observable<Card> getCardObservable(BriteDatabase db, long shipId, long cardTypeId){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME
                + " WHERE ship = "+shipId+" AND cardType = "+cardTypeId)
                .mapToOneOrDefault(cursor -> new Card(
                        cursor.getLong(0),
                        ShipAccessor.getShipObservable(db,cursor.getLong(1)),
                        CardTypeAccessor.getCardTypeObservable(db,cursor.getLong(2))
                ),null);
    }

    public static Card getCard(BriteDatabase db, long shipId, long cardTypeId){
        return getCardObservable(db,shipId,cardTypeId).blockingFirst(null);
    }
}
