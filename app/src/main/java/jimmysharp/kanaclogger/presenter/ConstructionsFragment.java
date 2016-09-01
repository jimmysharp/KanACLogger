package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;
import jimmysharp.kanaclogger.model.NewConstruction;
import jimmysharp.kanaclogger.model.table.Card;

public class ConstructionsFragment extends Fragment implements AddConstructionListener {
    private static String DIALOG_TAG = "addConstructionDialog";

    private ConstructionsRecyclerAdapter adapter;
    private DatabaseManager db = null;

    public ConstructionsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = ((MainActivity)getActivity()).getDB();
        this.adapter = new ConstructionsRecyclerAdapter(this.getActivity(),
                db.getAllShipConstructions());
        View view = inflater.inflate(R.layout.fragment_constructions, container, false);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.recyclerView_construction);
        ImageButton buttonOpenConstruction = (ImageButton) view.findViewById(R.id.button_open_construction);

        listView.setAdapter(adapter);
        buttonOpenConstruction.setOnClickListener(view1 -> {
            final AddConstructionDialog dialog = new AddConstructionDialog();
            dialog.setTargetFragment(this,100);
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

    @Override
    public void onAddConstruction(NewConstruction construction) {
        Card card = construction.getCard();
        db.addShipConstructionWithTransaction(
                card.getShip().getId(),
                card.getCardType().getId(),
                construction.getFuel(),
                construction.getBullet(),
                construction.getSteel(),
                construction.getBauxite());
        Toast.makeText(this.getActivity(),getString(R.string.msg_add_construction_success),Toast.LENGTH_SHORT).show();
    }
}
