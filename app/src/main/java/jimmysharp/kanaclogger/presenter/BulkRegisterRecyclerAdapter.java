package jimmysharp.kanaclogger.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.sqlbrite2.BriteDatabase;

import org.threeten.bp.ZonedDateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;
import jimmysharp.kanaclogger.model.NewConstruction;
import jimmysharp.kanaclogger.model.NewDrop;
import jimmysharp.kanaclogger.model.table.BattleType;
import jimmysharp.kanaclogger.model.table.Card;
import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.MapField;
import jimmysharp.kanaclogger.model.table.Ship;
import jimmysharp.kanaclogger.model.table.SubMap;

public class BulkRegisterRecyclerAdapter extends RecyclerView.Adapter {
    private final LayoutInflater inflater;
    private List<BulkRegisterViewItem> items;

    public BulkRegisterRecyclerAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        items = new LinkedList<>();
    }

    public void addNewConstruction(NewConstruction construction){
        items.add(new NewConstructionViewItem(construction));
        notifyDataSetChanged();
    }
    public void addNewDrop(NewDrop drop){
        items.add(new NewDropViewItem(drop));
        notifyDataSetChanged();
    }
    public void register(DatabaseManager db){
        ZonedDateTime date = ZonedDateTime.now();
        BriteDatabase dbRaw = db.getDatabase();

        if (items == null || items.size() == 0){
            Log.e("BulkAdapter","No insert item");
            throw new RuntimeException("No insert item");
        }

        BriteDatabase.Transaction transaction = dbRaw.newTransaction();
        try {
            for (BulkRegisterViewItem item : items) {
                item.register(db, date);
            }
            transaction.markSuccessful();
        } catch (RuntimeException e){
            Log.e("BulkAdapter","Failed to bulk insert: "+e.getMessage());
            throw new RuntimeException("Failed to bulk insert",e);
        } finally {
            transaction.end();
        }

        items.clear();
        notifyDataSetChanged();
    }

    public List<String> getItemString(){
        List<String> result = new LinkedList<>();
        for (BulkRegisterViewItem item : items){
            result.add(item.toString());
        }
        return result;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                return new NewConstructionViewHolder(
                        inflater.inflate(R.layout.item_bulk_construction,parent,false)
                );
            case 2:
                return new NewDropViewHolder(
                        inflater.inflate(R.layout.item_bulk_drop,parent,false)
                );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        items.get(position).setViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class NewConstructionViewHolder extends RecyclerView.ViewHolder{
        final TextView shipName;
        final TextView cardType;
        final TextView resources;

        public NewConstructionViewHolder(View itemView) {
            super(itemView);
            shipName = (TextView) itemView.findViewById(R.id.bulk_ship_name);
            cardType = (TextView) itemView.findViewById(R.id.bulk_card_type);
            resources = (TextView) itemView.findViewById(R.id.bulk_resources);
        }
    }

    public class NewDropViewHolder extends RecyclerView.ViewHolder{
        final TextView shipName;
        final TextView cardType;
        final TextView mapField;
        final TextView battleType;

        public NewDropViewHolder(View itemView) {
            super(itemView);
            shipName = (TextView) itemView.findViewById(R.id.bulk_ship_name);
            cardType = (TextView) itemView.findViewById(R.id.bulk_card_type);
            mapField = (TextView) itemView.findViewById(R.id.bulk_drop_map_field);
            battleType = (TextView) itemView.findViewById(R.id.bulk_drop_battle_type);
        }
    }

    public interface BulkRegisterViewItem {
        int getViewType();
        void register(DatabaseManager db, ZonedDateTime date);
        void setViewHolder(RecyclerView.ViewHolder holder);
    }

    public class NewConstructionViewItem implements BulkRegisterViewItem {
        private Card card;
        private Ship ship;
        private CardType cardType;
        private NewConstruction construction;

        public NewConstructionViewItem(NewConstruction construction){
            this.card = construction.getCard();
            this.construction = construction;
            this.ship = construction.getCard().getShip();
            this.cardType = construction.getCard().getCardType();
        }

        @Override
        public int getViewType() {
            return 1;
        }

        @Override
        public void register(DatabaseManager db, ZonedDateTime date) {
            db.addShipConstructionRaw(
                    date,
                    card.getId(),
                    construction.getFuel(),
                    construction.getBullet(),
                    construction.getSteel(),
                    construction.getBauxite());
        }

        @Override
        public void setViewHolder(RecyclerView.ViewHolder holder) {
            ((TextView)(holder.itemView.findViewById(R.id.bulk_ship_name))).setText(ship.getName());
            ((TextView)(holder.itemView.findViewById(R.id.bulk_card_type))).setText(cardType.getName());
            ((TextView)(holder.itemView.findViewById(R.id.bulk_resources))).setText(
                    String.format(Locale.US, "%d/%d/%d/%d",
                            construction.getFuel(),
                            construction.getBullet(),
                            construction.getSteel(),
                            construction.getBauxite()));
        }

        @Override
        public String toString() {
            return "建造 "+String.format(Locale.US, "%d/%d/%d/%d",
                    construction.getFuel(),
                    construction.getBullet(),
                    construction.getSteel(),
                    construction.getBauxite())
                    +" : "+ship.getName()+" "+cardType.getName();
        }
    }

    public class NewDropViewItem implements BulkRegisterViewItem {
        private Card card;
        private Ship ship;
        private CardType cardType;
        private SubMap subMap;
        private MapField mapField;
        private BattleType battleType;

        public NewDropViewItem(NewDrop drop){
            this.card = drop.getCard();
            this.ship = drop.getCard().getShip();
            this.cardType = drop.getCard().getCardType();
            this.subMap = drop.getSubMap();
            this.mapField = drop.getSubMap().getMapField();
            this.battleType = drop.getSubMap().getBattleType();
        }

        @Override
        public int getViewType() {
            return 2;
        }

        @Override
        public void register(DatabaseManager db, ZonedDateTime date) {
            db.addShipDropRaw(
                    date,
                    card.getId(),
                    subMap.getId());
        }

        @Override
        public void setViewHolder(RecyclerView.ViewHolder holder) {
            ((TextView)(holder.itemView.findViewById(R.id.bulk_ship_name))).setText(ship.getName());
            ((TextView)(holder.itemView.findViewById(R.id.bulk_card_type))).setText(cardType.getName());
            ((TextView)(holder.itemView.findViewById(R.id.bulk_drop_map_field))).setText(mapField.getIdName());
            ((TextView)(holder.itemView.findViewById(R.id.bulk_drop_battle_type))).setText(battleType.getName());
        }

        @Override
        public String toString() {
            return "ドロップ "+mapField.getIdName()+battleType.getName()
                    +" : "+ship.getName()+" "+cardType.getName();
        }
    }
}
