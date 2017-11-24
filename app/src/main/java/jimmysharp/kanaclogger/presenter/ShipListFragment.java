package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;

public class ShipListFragment extends Fragment {
    private ShipListRecyclerAdapter adapter;
    private DatabaseManager db = null;

    public ShipListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = ((MainActivity)getActivity()).getDB();
        this.adapter = new ShipListRecyclerAdapter(this.getActivity(),
                db.getSumObservable());
        View view = inflater.inflate(R.layout.fragment_ship_list, container, false);

        RecyclerView listView = view.findViewById(R.id.recyclerView_ship_list);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.db = null;
    }
}
