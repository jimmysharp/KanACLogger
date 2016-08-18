package jimmysharp.kanaclogger.model;

import android.database.SQLException;
import android.util.Log;
import com.squareup.sqlbrite.BriteDatabase;
import org.threeten.bp.ZonedDateTime;
import java.util.List;
import jimmysharp.kanaclogger.model.table.BattleType;
import jimmysharp.kanaclogger.model.table.BattleTypeAccessor;
import jimmysharp.kanaclogger.model.table.Card;
import jimmysharp.kanaclogger.model.table.CardAccessor;
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
import jimmysharp.kanaclogger.model.table.ShipType;
import jimmysharp.kanaclogger.model.table.ShipTypeAccessor;
import jimmysharp.kanaclogger.model.table.SubMap;
import jimmysharp.kanaclogger.model.table.SubMapAccessor;
import rx.Observable;

public class DatabaseManager {
    private final String TAG = DatabaseManager.class.getSimpleName();
    private BriteDatabase db;

    public DatabaseManager(BriteDatabase db){
        this.db = db;
    }

    public void addShipConstructionWithTransaction(ZonedDateTime date, long shipId, long cardTypeId, long fuel, long bullet, long steel, long bauxite){
        Card card = CardAccessor.getCard(db,shipId,cardTypeId);
        if (card == null){
            throw new RuntimeException("No such Card");
        }

        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            long shipTransactionId = ShipTransactionAccessor.insert(db, date,card.getId(), 1);
            ShipConstructionAccessor.insert(db,shipTransactionId,fuel,bullet,steel,bauxite);
            transaction.markSuccessful();
        } catch (SQLException e) {
            Log.e(TAG,"Failed to insert new construction: "+e.getMessage());
            throw new RuntimeException("Failed to insert new construction",e);
        } finally {
            transaction.end();
        }
    }

    public void addShipConstructionWithTransaction(long shipId, long cardTypeId, long fuel, long bullet, long steel, long bauxite){
        this.addShipConstructionWithTransaction(ZonedDateTime.now(),shipId,cardTypeId,fuel,bullet,steel,bauxite);
    }

    public void addShipConstructionRaw(ZonedDateTime date, long cardId, long fuel, long bullet, long steel, long bauxite){
        try {
            long shipTransactionId = ShipTransactionAccessor.insert(db, date,cardId, 1);
            ShipConstructionAccessor.insert(db,shipTransactionId,fuel,bullet,steel,bauxite);
        } catch (SQLException e) {
            Log.e(TAG,"Failed to insert new construction: "+e.getMessage());
            throw new RuntimeException("Failed to insert new construction",e);
        }
    }

    public void addShipConstructionRaw(long cardId, long fuel, long bullet, long steel, long bauxite){
        this.addShipConstructionRaw(ZonedDateTime.now(),cardId,fuel,bullet,steel,bauxite);
    }

    public void addShipDropWithTransaction(ZonedDateTime date, long shipId, long cardTypeId, long subMapId){
        Card card = CardAccessor.getCard(db,shipId,cardTypeId);
        if (card == null){
            throw new RuntimeException("No such Card");
        }

        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            long shipTransactionId = ShipTransactionAccessor.insert(db, date,card.getId(),1);
            ShipDropAccessor.insert(db,shipTransactionId,subMapId);
            transaction.markSuccessful();
        } catch (SQLException e) {
            Log.e(TAG,"Failed to insert new drop: "+e.getMessage());
            throw new RuntimeException("Failed to insert new drop",e);
        } finally {
            transaction.end();
        }
    }

    public void addShipDropWithTransaction(long shipId, long cardTypeId, long subMapId){
        this.addShipDropWithTransaction(ZonedDateTime.now(),shipId,cardTypeId,subMapId);
    }

    public void addShipDropRaw(ZonedDateTime date, long cardId, long subMapId){
        try {
            long shipTransactionId = ShipTransactionAccessor.insert(db, date,cardId,1);
            ShipDropAccessor.insert(db,shipTransactionId,subMapId);
        } catch (SQLException e) {
            Log.e(TAG,"Failed to insert new drop: "+e.getMessage());
            throw new RuntimeException("Failed to insert new drop",e);
        }
    }

    public void addShipDropRaw(long cardId, long subMapId){
        this.addShipDropRaw(ZonedDateTime.now(),cardId,subMapId);
    }

    public Observable<SubMap> getSubMap(long mapFieldId, long battleTypeId){
        return SubMapAccessor.getSubMapObservable(db,mapFieldId,battleTypeId);
    }

    public Observable<List<Ship>> getAllShips(){
        return ShipAccessor.getAllShipsObservable(db);
    }

    public Observable<List<Ship>> getAllShipsSorted() {
        return ShipAccessor.getAllShipsSortedObservable(db);
    }

    public Observable<List<Ship>> getShips(ShipType shipType, Boolean remodelled){
        return ShipAccessor.getShipsObservable(db,shipType,remodelled);
    }

    public Observable<List<CardType>> getAllCardTypes() {
        return CardTypeAccessor.getAllCardTypesObservable(db);
    }

    public Observable<List<ShipConstruction>> getAllShipConstructions() {
        return ShipConstructionAccessor.getAllShipConstructionsObservable(db);
    }

    public Observable<List<ShipDrop>> getAllShipDrops(){
        return ShipDropAccessor.getAllShipDropsObservable(db);
    }

    public Observable<List<BattleType>> getAllBattleTypes(){
        return BattleTypeAccessor.getAllBattleTypesObservable(db);
    }

    public Observable<List<MapField>> getAllMapFields(){
        return MapFieldAccessor.getAllMapFieldsObservable(db);
    }

    public Observable<List<ShipType>> getAllShipTypes(){
        return ShipTypeAccessor.getAllShipTypesObservable(db);
    }

    public Observable<List<ShipTransactionSum>> getSumObservable(){
        return db.createQuery("ShipTransaction", "SELECT * FROM Card" +
                " LEFT JOIN (SELECT card, SUM(quantity) FROM ShipTransaction GROUP BY card) ON Card._id = card" +
                " INNER JOIN Ship ON ship = Ship._id" +
                " ORDER BY Ship.sortId, Ship.remodelled")
                .mapToList(cursor -> new ShipTransactionSum(
                        cursor.getLong(0),
                        ShipAccessor.getShip(db,cursor.getLong(1)),
                        CardTypeAccessor.getCardType(db,cursor.getLong(2)),
                        cursor.isNull(4) ? 0 : cursor.getLong(4)
                ));
    }

    public Card getCard(long shipId, long cardTypeId){
        return CardAccessor.getCard(db,shipId,cardTypeId);
    }

    public BriteDatabase getDatabase(){
        return db;
    }
}
