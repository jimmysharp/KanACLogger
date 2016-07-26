package jimmysharp.kanaclogger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import jimmysharp.kanaclogger.model.ShipConstruction;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class ShipConstructionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Observable<List<ShipConstruction>> itemsObservable;
    private Subscription itemsSubscription;
    private List<ShipConstruction> items;
    private final LayoutInflater inflater;

    public ShipConstructionRecyclerAdapter(Context context, Observable<List<ShipConstruction>> items){
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
            //TODO: Bind item to viewholder
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemsSubscription = itemsObservable.subscribeOn(Schedulers.io()).subscribe(new Observer<List<ShipConstruction>>() {
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
        public ShipConstructionRecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
