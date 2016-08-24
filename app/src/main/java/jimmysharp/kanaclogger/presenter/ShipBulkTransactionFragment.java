package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;
import jimmysharp.kanaclogger.model.NewConstruction;
import jimmysharp.kanaclogger.model.NewDrop;
import jimmysharp.kanaclogger.model.TwitterManager;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import twitter4j.Status;

public class ShipBulkTransactionFragment extends Fragment implements AddTypeSelectListener,
        AddShipConstructionListener, AddShipDropListener {
    private static final String TAG = "BulkTransFragment";
    private static String DIALOG_CONSTRUCTION_TAG = "addConstructionDialog";
    private static String DIALOG_DROP_TAG = "addDropDialog";
    private static String DIALOG_SELECT_TAG = "addTypeSelectDialog";

    private CompositeSubscription subscription;
    private ShipBulkTransactionRecyclerAdapter adapter = null;
    private DatabaseManager db = null;
    private TwitterManager twitterManager = null;
    private SharedPreferences preferences = null;

    public ShipBulkTransactionFragment() { }

    private void deleteDialog(String tag){
        Fragment dialog = getChildFragmentManager().findFragmentByTag(tag);
        if (dialog != null) {
            getChildFragmentManager().beginTransaction().remove(dialog).commit();
        }
    }

    @Override
    public void onPause() {
        deleteDialog(DIALOG_CONSTRUCTION_TAG);
        deleteDialog(DIALOG_DROP_TAG);
        deleteDialog(DIALOG_SELECT_TAG);

        subscription.unsubscribe();
        subscription = new CompositeSubscription();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ship_bulk_transaction, container, false);
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.recyclerView_bulk_transaction);
        ImageButton button = (ImageButton) view.findViewById(R.id.button_open_add_select);

        subscription = new CompositeSubscription();
        twitterManager = new TwitterManager(getActivity());
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        db = ((MainActivity)getActivity()).getDB();
        adapter = new ShipBulkTransactionRecyclerAdapter(getActivity());
        listView.setAdapter(adapter);

        button.setOnClickListener(view1 -> {
            final AddTypeSelectDialog dialog = new AddTypeSelectDialog();
            dialog.setTargetFragment(this,100);
            dialog.show(getChildFragmentManager(),DIALOG_SELECT_TAG);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        db = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(twitterManager != null){
            twitterManager.dispose();
            twitterManager = null;
        }
    }

    @Override
    public void onAddConstruction(NewConstruction construction) {
        adapter.addNewConstruction(construction);
    }

    @Override
    public void onAddNewDrop(NewDrop drop) {
        adapter.addNewDrop(drop);
    }

    @Override
    public void onConstructionSelected() {
        final AddShipConstructionDialog dialog = new AddShipConstructionDialog();
        dialog.setTargetFragment(this,100);
        dialog.show(getChildFragmentManager(),DIALOG_CONSTRUCTION_TAG);
    }

    @Override
    public void onDropSelected() {
        final AddShipDropDialog dialog = new AddShipDropDialog();
        dialog.setTargetFragment(this,100);
        dialog.show(getChildFragmentManager(),DIALOG_DROP_TAG);
    }

    @Override
    public void onSaveSelected() {
        try{
            List<String> items = adapter.getItemString();
            adapter.register(db);
            Toast.makeText(this.getActivity(),getString(R.string.msg_bulk_insert_success),Toast.LENGTH_SHORT).show();

            Log.v(TAG,"items.size:"+items.size());
            Log.v(TAG,"isAccessTokenStored:"+twitterManager.isAccessTokenStored());
            Log.v(TAG,"isTweetEnabled:"+preferences.getBoolean("is_tweet_enabled",false));

            if (items != null
                    && items.size() > 0
                    && twitterManager.isAccessTokenStored()
                    && preferences.getBoolean("is_tweet_enabled",false)
                    ){
                Log.v(TAG,"subscribe prepare");
                String hashtag = preferences.getString("hashtag_text","");

                List<Single<Status>> tweets = twitterManager.tweet(items,hashtag);
                for (Single<Status> tweet : tweets) {
                    subscription.add(tweet
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleSubscriber<Status>() {
                        @Override
                        public void onSuccess(Status value) {
                            Toast.makeText(getActivity(),getString(R.string.msg_twitter_tweet_success)+"\n"+value.getText()
                                    ,Toast.LENGTH_SHORT).show();
                            Log.v(TAG,"Success to tweet:"+value.getText());
                        }
                        @Override
                        public void onError(Throwable error) {
                            Toast.makeText(getActivity(),getString(R.string.msg_twitter_tweet_failed),Toast.LENGTH_LONG).show();
                            Log.e(TAG,"Failed to tweet",error);
                        }
                    }));
                }
            }
        } catch (RuntimeException e){
            Log.e(TAG,"Failed to bulk insert: "+e.getMessage());
            Toast.makeText(this.getActivity(),getString(R.string.msg_bulk_insert_failed),Toast.LENGTH_LONG).show();
        }
    }
}
