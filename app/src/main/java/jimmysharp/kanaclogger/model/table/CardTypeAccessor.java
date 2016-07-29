package jimmysharp.kanaclogger.model.table;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;

public class CardTypeAccessor {
    private static final String TABLE_NAME = "CardType";
    public static final String SQL_CREATE =
            "CREATE TABLE \"CardType\" (\n" +
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

    public static Observable<List<CardType>> getAllCardTypes(BriteDatabase db){
        return db.createQuery(TABLE_NAME, "SELECT * FROM " + TABLE_NAME)
                .mapToList(cursor -> new CardType(cursor.getLong(0),cursor.getString(1)));
    }
    public static Observable<CardType> getCardType(BriteDatabase db, long id){
        return db.createQuery(TABLE_NAME, "SELECT * FROM "+ TABLE_NAME
                + " WHERE _id = "+id)
                .mapToOneOrDefault(cursor -> new CardType(cursor.getLong(0),cursor.getString(1)), null);
    }
}
