package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;

public class ShipBulkTransactionFragment extends Fragment implements AddTypeSelectListener,
        AddShipConstructionListener, AddShipDropListener {
    private static String DIALOG_CONSTRUCTION_TAG = "addConstructionDialog";
    private static String DIALOG_DROP_TAG = "addDropDialog";
    private static String DIALOG_SELECT_TAG = "addTypeSelectDialog";

    private ShipBulkTransactionRecyclerAdapter adapter = null;
    private DatabaseManager db = null;

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
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ship_bulk_transaction, container, false);
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.recyclerView_bulk_transaction);
        ImageButton button = (ImageButton) view.findViewById(R.id.button_open_add_select);

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
            adapter.register(db);
            Toast.makeText(this.getActivity(),getString(R.string.msg_bulk_insert_success),Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e){
            Log.e("BulkFragment","Failed to bulk insert: "+e.getMessage());
            Toast.makeText(this.getActivity(),getString(R.string.msg_bulk_insert_failed),Toast.LENGTH_LONG).show();
        }
    }
}
