package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

public class MapAreaAccessor {
    private static final String TABLE_NAME = "MapArea";
    private static final String SQL_CREATE =
            "CREATE TABLE \"MapArea\" (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`name`\tTEXT NOT NULL,\n" +
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
