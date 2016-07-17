package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

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
}
