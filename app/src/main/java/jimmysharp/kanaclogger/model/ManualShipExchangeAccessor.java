package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

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
}
