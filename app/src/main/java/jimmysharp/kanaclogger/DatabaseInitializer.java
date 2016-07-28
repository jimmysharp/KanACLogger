package jimmysharp.kanaclogger;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jimmysharp.kanaclogger.model.BattleTypeAccessor;
import jimmysharp.kanaclogger.model.CardTypeAccessor;
import jimmysharp.kanaclogger.model.KanACDataMetaAccessor;
import jimmysharp.kanaclogger.model.ManualShipExchangeAccessor;
import jimmysharp.kanaclogger.model.ManualShipExchangeTypeAccessor;
import jimmysharp.kanaclogger.model.MapAreaAccessor;
import jimmysharp.kanaclogger.model.MapFieldAccessor;
import jimmysharp.kanaclogger.model.ShipAccessor;
import jimmysharp.kanaclogger.model.ShipConstructionAccessor;
import jimmysharp.kanaclogger.model.ShipDropAccessor;
import jimmysharp.kanaclogger.model.ShipTransactionAccessor;
import jimmysharp.kanaclogger.model.ShipTypeAccessor;
import jimmysharp.kanaclogger.model.SubMapAccessor;
import rx.schedulers.Schedulers;

public class DatabaseInitializer {
    private static final String TAG = DatabaseInitializer.class.getSimpleName();
    public static final String BUNDLED_DB = "kanacdb.db";
    private static final String KANACDB_ARIAS = "kanac";
    private static final String TEMPDB_PREFIX = "kanactemp";
    private static final int DATA_VERSION = 1;

    private Context context;
    private SqlBrite sqlBrite;
    private BriteDatabase userDB;

    public DatabaseInitializer(Context context){
        this.context = context;
    }

    public void migrate() throws IOException{
        Log.v(TAG,"Schema migration start");
        SQLiteDatabase db = new UserDBOpenHelper(context).getWritableDatabase();
        Log.v(TAG,"Schema migration end");

        Log.v(TAG,"Data migration start");
        Integer dataVersion = KanACDataMetaAccessor.getDataVersion(db);

        try {
            if (dataVersion == null || dataVersion < DATA_VERSION) {
                Log.v(TAG, "Local DB data version is old : local is "+dataVersion+", bundled is "+DATA_VERSION);
                Log.v(TAG, "Start loading bundled DB");
                loadBundledDB(db);
            } else {
                Log.v(TAG,"Current DB data version is up-to-date");
                Log.v(TAG,"Skipping update process");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
        Log.v(TAG,"Data migration end");
    }

    public BriteDatabase open() throws IOException{
        Log.v(TAG,"Open DB to use");
        this.migrate();
        sqlBrite = SqlBrite.create();
        userDB = sqlBrite.wrapDatabaseHelper(new UserDBOpenHelper(context), Schedulers.io());

        return userDB;
    }

    public void close(){
        Log.v(TAG,"Close DB to use");
        userDB.close();
        sqlBrite = null;
    }

    private void loadBundledDB(SQLiteDatabase db) throws IOException{
        String[] copyTables = {
                BattleTypeAccessor.getTableName(),
                CardTypeAccessor.getTableName(),
                MapAreaAccessor.getTableName(),
                MapFieldAccessor.getTableName(),
                SubMapAccessor.getTableName(),
                KanACDataMetaAccessor.getTableName(),
                ManualShipExchangeTypeAccessor.getTableName(),
                ShipTypeAccessor.getTableName(),
                ShipAccessor.getTableName()
        };
        String tempDB = copyBundledDB();

        db.execSQL("ATTACH DATABASE `"
                + tempDB
                + "` as "
                + KANACDB_ARIAS
        );
        Log.v(TAG,"Temp DB attached");
        if (db.getAttachedDbs().size() < 2) {
            Log.e(TAG,"Failed to attach DB: "+tempDB);
            throw new IOException("Failed to attach DB: "+tempDB);
        }

        Log.v(TAG,"Copy tables start");
        db.beginTransaction();
        for (String table : copyTables){
            db.execSQL("INSERT OR REPLACE INTO `"+table+"` SELECT * FROM "+KANACDB_ARIAS+"."+table);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.v(TAG,"Copy tables end");

        db.execSQL("DETACH DATABASE "+KANACDB_ARIAS);
        Log.v(TAG,"Temp DB detached");
    }

    private String copyBundledDB() throws IOException {
        InputStream kanACDataStream = null;
        OutputStream tempDBStream = null;
        File tempFile = null;
        String tempDBDir = null;
        byte[] buffer = new byte[1024];
        int size;

        try {
            kanACDataStream = context.getAssets().open(BUNDLED_DB, AssetManager.ACCESS_STREAMING);
            Log.v(TAG,"Successfully open bundled DB: "+BUNDLED_DB);
            tempFile = File.createTempFile(TEMPDB_PREFIX,".db",context.getCacheDir());
            Log.v(TAG,"Successfully make temp DB: "+tempFile.getAbsolutePath());
            tempDBStream = new FileOutputStream(tempFile);
            Log.v(TAG,"Successfully open temp DB: "+tempFile.getAbsolutePath());
            tempDBDir = tempFile.getAbsolutePath();

            while ((size = kanACDataStream.read(buffer)) > 0){
                tempDBStream.write(buffer, 0, size);
            }
            tempDBStream.flush();
            tempDBStream.close();
            kanACDataStream.close();
            Log.v(TAG,"Successfully copy bundled DB to temp DB.");

            return tempDBDir;
        } catch (IOException e) {
            Log.e(TAG,"Failed to copy bundled DB.");
            throw e;
        } finally {
            try {
                if (kanACDataStream != null) kanACDataStream.close();
            } catch (IOException e1) {}
            try {
                if (tempDBStream != null) tempDBStream.close();
            } catch (IOException e1) {}
        }
    }

    public void dispose(){
        this.context = null;
    }

    private class UserDBOpenHelper extends SQLiteOpenHelper{
        private final String TAG = UserDBOpenHelper.class.getSimpleName();
        private static final String USER_DB = "userdb.db";
        private static final int VERSION = 1;

        public UserDBOpenHelper(Context context) {
            super(context, USER_DB, null, VERSION);
            Log.v(TAG,"Create new UserDBOpenHelper");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.v(TAG,"Create tables start");
            ShipTypeAccessor.create(sqLiteDatabase);
            ShipAccessor.create(sqLiteDatabase);
            CardTypeAccessor.create(sqLiteDatabase);
            MapAreaAccessor.create(sqLiteDatabase);
            MapFieldAccessor.create(sqLiteDatabase);
            BattleTypeAccessor.create(sqLiteDatabase);
            SubMapAccessor.create(sqLiteDatabase);
            ManualShipExchangeTypeAccessor.create(sqLiteDatabase);

            ShipTransactionAccessor.create(sqLiteDatabase);
            ShipDropAccessor.create(sqLiteDatabase);
            ShipConstructionAccessor.create(sqLiteDatabase);
            ManualShipExchangeAccessor.create(sqLiteDatabase);
            KanACDataMetaAccessor.create(sqLiteDatabase);
            Log.v(TAG,"Create tables end");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
