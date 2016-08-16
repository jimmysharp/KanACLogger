package jimmysharp.kanaclogger.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.table.ShipDrop;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ShipDropsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Observable<List<ShipDrop>> itemsObservable;
    private Subscription itemsSubscription;
    private List<ShipDrop> items;
    private final LayoutInflater inflater;

    public ShipDropsRecyclerAdapter(Context context, Observable<List<ShipDrop>> items){
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.itemsObservable = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShipDropsRecyclerViewHolder(
                inflater.inflate(R.layout.item_drop,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (items != null && items.size() > position && items.get(position) != null){
            ShipDrop item = items.get(items.size()-position-1);
            item.getShipTransaction().observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(shipTransaction -> {
                shipTransaction.getShip().observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(ship ->
                    ((TextView)(holder.itemView.findViewById(R.id.ship_name))).setText(ship.getName()));
                ((TextView)(holder.itemView.findViewById(R.id.date))).setText(
                        shipTransaction.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            });
            item.getSubMap().observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(subMap -> {
                subMap.getMapField().observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(mapField ->
                        ((TextView)(holder.itemView.findViewById(R.id.drop_map_field))).setText(mapField.getIdName()));
                subMap.getBattleType().observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(battleType ->
                        ((TextView)(holder.itemView.findViewById(R.id.drop_battle_type))).setText(battleType.getName()));
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemsSubscription = itemsObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ShipDrop>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<ShipDrop> shipDrop) {
                items = shipDrop;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.itemsSubscription.unsubscribe();
    }

    public class ShipDropsRecyclerViewHolder extends RecyclerView.ViewHolder{
        final TextView shipName;
        final TextView mapField;
        final TextView battleType;
        final TextView date;

        public ShipDropsRecyclerViewHolder(View itemView) {
            super(itemView);
            shipName = (TextView) itemView.findViewById(R.id.ship_name);
            mapField = (TextView) itemView.findViewById(R.id.drop_map_field);
            battleType = (TextView) itemView.findViewById(R.id.drop_battle_type);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}