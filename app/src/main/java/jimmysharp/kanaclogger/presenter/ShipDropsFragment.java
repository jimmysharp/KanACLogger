package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;

public class ShipDropsFragment extends Fragment {
    private static String DIALOG_TAG = "addDropDialog";

    private ShipDropsRecyclerAdapter adapter;
    private DatabaseManager db = null;

    public ShipDropsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = ((MainActivity)getActivity()).getDB();
        this.adapter = new ShipDropsRecyclerAdapter(this.getActivity(),
                db.getAllShipDrops());

        View view = inflater.inflate(R.layout.fragment_ship_drops, container, false);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.recyclerView_drop);
        ImageButton buttonOpenDrop = (ImageButton) view.findViewById(R.id.button_open_drop);

        listView.setAdapter(adapter);
        buttonOpenDrop.setOnClickListener(view1 -> {
            final AddShipDropDialog dialog = new AddShipDropDialog();
            dialog.setTargetFragment(this,101);
            dialog.show(getChildFragmentManager(),DIALOG_TAG);
        });

        return view;
    }

    @Override
    public void onPause() {
        Fragment dialog = getChildFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (dialog != null) {
            getChildFragmentManager().beginTransaction().remove(dialog).commit();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.db = null;
    }
}