package jimmysharp.kanaclogger.model;

import android.database.sqlite.SQLiteDatabase;

public class ShipConstructionAccessor {
    private static final String TABLE_NAME = "ShipConstruction";
    private static final String SQL_CREATE =
            "CREATE TABLE \"ShipConstruction\" (\n" +
                    "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`shipTransaction`\tINTEGER NOT NULL UNIQUE,\n" +
                    "\t`fuel`\tINTEGER NOT NULL,\n" +
                    "\t`bullet`\tINTEGER NOT NULL,\n" +
                    "\t`steel`\tINTEGER NOT NULL,\n" +
                    "\t`bauxite`\tINTEGER NOT NULL\n" +
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
