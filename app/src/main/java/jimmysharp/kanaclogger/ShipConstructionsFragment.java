package jimmysharp.kanaclogger;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.sqlbrite.BriteDatabase;

import jimmysharp.kanaclogger.model.ShipConstructionAccessor;

public class ShipConstructionsFragment extends Fragment {
    private ShipConstructionsRecyclerAdapter adapter;
    private BriteDatabase db = null;

    public ShipConstructionsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db = ((MainActivity)getActivity()).getDB();
        this.adapter = new ShipConstructionsRecyclerAdapter(this.getActivity(),
                ShipConstructionAccessor.getAllShipConstructions(db));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ship_constructions, container, false);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.recyclerView_construction);
        ImageButton buttonOpenConstruction = (ImageButton) view.findViewById(R.id.button_open_construction);

        listView.setAdapter(adapter);
        buttonOpenConstruction.setOnClickListener(view1 -> {
            final AddShipConstructionDialog dialog = new AddShipConstructionDialog();
            dialog.setTargetFragment(this,100);
            dialog.show(getChildFragmentManager(),"addConstructionDialog");
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.db = null;
    }


}
