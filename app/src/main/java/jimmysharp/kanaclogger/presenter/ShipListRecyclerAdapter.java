package jimmysharp.kanaclogger.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.ShipTransactionSum;
import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.Ship;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ShipListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Observable<List<ShipTransactionSum>> itemsObservable;
    private Subscription itemsSubscription;
    private List<ShipTransactionSum> items;
    private final LayoutInflater inflater;

    public ShipListRecyclerAdapter(Context context, Observable<List<ShipTransactionSum>> items){
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.itemsObservable = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShipListRecyclerViewHolder(
                inflater.inflate(R.layout.item_ship_count,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (items != null && items.size() > position && items.get(position) != null){
            ShipTransactionSum item = items.get(position);
            Ship ship = item.getShip();
            CardType cardType = item.getCardType();

            ((TextView)(holder.itemView.findViewById(R.id.ship_name))).setText(ship.getName() + " " + cardType.getName());
            ((TextView)(holder.itemView.findViewById(R.id.ship_count))).setText(String.valueOf(item.getQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemsSubscription = itemsObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ShipTransactionSum>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<ShipTransactionSum> shipCounts) {
                items = shipCounts;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.itemsSubscription.unsubscribe();
    }

    public class ShipListRecyclerViewHolder extends RecyclerView.ViewHolder{
        final TextView shipName;
        final TextView shipCount;

        public ShipListRecyclerViewHolder(View itemView) {
            super(itemView);
            shipName = (TextView) itemView.findViewById(R.id.ship_name);
            shipCount = (TextView) itemView.findViewById(R.id.ship_count);
        }
    }
}