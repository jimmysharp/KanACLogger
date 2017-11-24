package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import org.threeten.bp.ZonedDateTime;
import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;
import jimmysharp.kanaclogger.model.NewDrop;
import jimmysharp.kanaclogger.model.table.Card;

public class DropsFragment extends Fragment implements AddDropListener {
    private static String DIALOG_TAG = "addDropDialog";

    private DropsRecyclerAdapter adapter;
    private DatabaseManager db = null;

    public DropsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = ((MainActivity)getActivity()).getDB();
        this.adapter = new DropsRecyclerAdapter(this.getActivity(),
                db.getAllShipDrops());

        View view = inflater.inflate(R.layout.fragment_drops, container, false);

        RecyclerView listView = view.findViewById(R.id.recyclerView_drop);
        ImageButton buttonOpenDrop = view.findViewById(R.id.button_open_drop);

        listView.setAdapter(adapter);
        buttonOpenDrop.setOnClickListener(view1 -> {
            final AddDropDialog dialog = new AddDropDialog();
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

    @Override
    public void onAddNewDrop(NewDrop drop) {
        Card card = drop.getCard();
        db.addShipDropWithTransaction(ZonedDateTime.now(),card.getShip().getId(),card.getCardType().getId(),drop.getSubMap().getId());
        Toast.makeText(this.getActivity(),getString(R.string.msg_add_drop_success),Toast.LENGTH_SHORT).show();
    }
}