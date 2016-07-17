package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

public class SubMapAccessor {
    private static final String TABLE_NAME = "SubMap";
    private static final String SQL_CREATE =
            "CREATE TABLE \"SubMap\" (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`mapField`\tINTEGER NOT NULL,\n" +
                    "\t`battleType`\tINTEGER NOT NULL,\n" +
                    "\t`gp`\tINTEGER NOT NULL,\n" +
                    "\t`seconds`\tINTEGER NOT NULL,\n" +
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
