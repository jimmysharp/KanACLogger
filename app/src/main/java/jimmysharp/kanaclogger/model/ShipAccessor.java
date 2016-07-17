package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

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
}
