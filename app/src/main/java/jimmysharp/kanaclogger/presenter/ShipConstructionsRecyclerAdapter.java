package jimmysharp.kanaclogger.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.table.Card;
import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.Ship;
import jimmysharp.kanaclogger.model.table.ShipConstruction;
import jimmysharp.kanaclogger.model.table.ShipTransaction;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ShipConstructionsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Observable<List<ShipConstruction>> itemsObservable;
    private Subscription itemsSubscription;
    private List<ShipConstruction> items;
    private final LayoutInflater inflater;

    public ShipConstructionsRecyclerAdapter(Context context, Observable<List<ShipConstruction>> items){
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.itemsObservable = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShipConstructionRecyclerViewHolder(
                inflater.inflate(R.layout.item_construction,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (items != null && items.size() > position && items.get(position) != null){
            ShipConstruction item = items.get(items.size()-position-1);
            ShipTransaction transaction = item.getShipTransaction();
            Card card = transaction.getCard();
            CardType cardType = card.getCardType();
            Ship ship = card.getShip();

            ((TextView)(holder.itemView.findViewById(R.id.ship_name))).setText(ship.getName());
            ((TextView)(holder.itemView.findViewById(R.id.card_type))).setText(cardType.getName());
            ((TextView)(holder.itemView.findViewById(R.id.date))).setText(
                    transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            ((TextView)(holder.itemView.findViewById(R.id.resources))).setText(
                    String.format(Locale.US, "%d/%d/%d/%d",item.getFuel(),item.getBullet(),item.getSteel(),item.getBauxite())
            );
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemsSubscription = itemsObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ShipConstruction>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<ShipConstruction> shipConstructionQueryItems) {
                items = shipConstructionQueryItems;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.itemsSubscription.unsubscribe();
    }

    public class ShipConstructionRecyclerViewHolder extends RecyclerView.ViewHolder{
        final TextView shipName;
        final TextView cardType;
        final TextView date;
        final TextView resources;

        public ShipConstructionRecyclerViewHolder(View itemView) {
            super(itemView);
            shipName = (TextView) itemView.findViewById(R.id.ship_name);
            cardType = (TextView) itemView.findViewById(R.id.card_type);
            date = (TextView) itemView.findViewById(R.id.date);
            resources = (TextView) itemView.findViewById(R.id.resources);
        }
    }
}
