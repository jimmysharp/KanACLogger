package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

public class MapFieldAccessor {
    private static final String TABLE_NAME = "MapField";
    private static final String SQL_CREATE =
            "CREATE TABLE \"MapField\" (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`mapArea`\tINTEGER NOT NULL,\n" +
                    "\t`number`\tINTEGER NOT NULL,\n" +
                    "\t`idName`\tTEXT NOT NULL,\n" +
                    "\t`name`\tTEXT NOT NULL,\n" +
                    "\t`operationName`\tTEXT NOT NULL,\n" +
                    "\t`operationComment`\tTEXT NOT NULL,\n" +
                    "\t`level`\tINTEGER NOT NULL,\n" +
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
