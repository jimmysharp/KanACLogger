package jimmysharp.kanaclogger.model;

import android.database.SQLException;
import android.util.Log;
import com.squareup.sqlbrite.BriteDatabase;
import org.threeten.bp.ZonedDateTime;
import java.util.List;

import jimmysharp.kanaclogger.model.table.BattleType;
import jimmysharp.kanaclogger.model.table.BattleTypeAccessor;
import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.CardTypeAccessor;
import jimmysharp.kanaclogger.model.table.MapField;
import jimmysharp.kanaclogger.model.table.MapFieldAccessor;
import jimmysharp.kanaclogger.model.table.Ship;
import jimmysharp.kanaclogger.model.table.ShipAccessor;
import jimmysharp.kanaclogger.model.table.ShipConstruction;
import jimmysharp.kanaclogger.model.table.ShipConstructionAccessor;
import jimmysharp.kanaclogger.model.table.ShipDrop;
import jimmysharp.kanaclogger.model.table.ShipDropAccessor;
import jimmysharp.kanaclogger.model.table.ShipTransactionAccessor;
import jimmysharp.kanaclogger.model.table.SubMap;
import jimmysharp.kanaclogger.model.table.SubMapAccessor;
import rx.Observable;

public class DatabaseManager {
    private final String TAG = DatabaseManager.class.getSimpleName();
    private BriteDatabase db;

    public DatabaseManager(BriteDatabase db){
        this.db = db;
    }

    public void addShipConstruction(ZonedDateTime date, long shipId, long cardTypeId, long fuel, long bullet, long steel,long bauxite){
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            long shipTransactionId = ShipTransactionAccessor.insert(db, date,shipId,cardTypeId,1);
            ShipConstructionAccessor.insert(db,shipTransactionId,fuel,bullet,steel,bauxite);
            transaction.markSuccessful();
        } catch (SQLException e) {
            Log.e(TAG,"Failed to insert new construction: "+e.getMessage());
            throw new RuntimeException("Failed to insert new construction",e);
        } finally {
            transaction.end();
        }
    }

    public void addShipConstruction(long shipId, long cardTypeId, long fuel,long bullet, long steel, long bauxite){
        this.addShipConstruction(ZonedDateTime.now(),shipId,cardTypeId,fuel,bullet,steel,bauxite);
    }

    public void addShipDrop(ZonedDateTime date, long shipId, long cardTypeId, long subMapId){
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            long shipTransactionId = ShipTransactionAccessor.insert(db, date,shipId,cardTypeId,1);
            ShipDropAccessor.insert(db,shipTransactionId,subMapId);
            transaction.markSuccessful();
        } catch (SQLException e) {
            Log.e(TAG,"Failed to insert new drop: "+e.getMessage());
            throw new RuntimeException("Failed to insert new drop",e);
        } finally {
            transaction.end();
        }
    }

    public void addShipDrop(long shipId, long cardTypeId, long subMapId){
        this.addShipDrop(ZonedDateTime.now(),shipId,cardTypeId,subMapId);
    }

    public Observable<SubMap> getSubMap(long mapFieldId, long battleTypeId){
        return SubMapAccessor.getSubMap(db,mapFieldId,battleTypeId);
    }

    public Observable<List<Ship>> getAllShips(){
        return ShipAccessor.getAllShips(db);
    }

    public Observable<List<CardType>> getAllCardTypes() {
        return CardTypeAccessor.getAllCardTypes(db);
    }

    public Observable<List<ShipConstruction>> getAllShipConstructions() {
        return ShipConstructionAccessor.getAllShipConstructions(db);
    }

    public Observable<List<ShipDrop>> getAllShipDrops(){
        return ShipDropAccessor.getAllShipDrops(db);
    }

    public Observable<List<BattleType>> getAllBattleTypes(){
        return BattleTypeAccessor.getAllBattleTypes(db);
    }

    public Observable<List<MapField>> getAllMapFields(){
        return MapFieldAccessor.getAllMapFields(db);
    }
}
