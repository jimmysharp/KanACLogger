package jimmysharp.kanaclogger.model.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class KanACDataMetaAccessor {
    private static final String TABLE_NAME = "KanACDataMeta";
    private static String SQL_CREATE =
            "CREATE TABLE `KanACDataMeta` (\n" +
                    "\t`key`\tTEXT,\n" +
                    "\t`value`\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(key)\n" +
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

    public static Integer getDataVersion(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT value from KanACDataMeta WHERE key = \'version\'",null);
        if (cursor.getCount() < 0) {
            cursor.close();
            return null;
        }
        else{
            cursor.moveToFirst();
            int version = cursor.getInt(0);
            cursor.close();
            return version;
        }
    }
}
