package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

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
}
