package jimmysharp.kanaclogger.presenter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;
import jimmysharp.kanaclogger.model.table.BattleType;
import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.MapField;
import jimmysharp.kanaclogger.model.table.Ship;
import jimmysharp.kanaclogger.model.table.ShipType;
import jimmysharp.kanaclogger.model.table.SubMap;
import rx.Subscription;

public class AddShipDropDialog extends DialogFragment {
    private String TAG = AddShipDropDialog.class.getSimpleName();

    private MapFieldsAdapter mapFields = null;
    private BattleTypesAdapter battleTypes = null;
    private ShipTypesAdapter shipTypes = null;
    private ShipsAdapter ships = null;
    private CardTypesAdapter cardTypes = null;
    private DatabaseManager db = null;

    private Spinner spinnerMapFields;
    private Spinner spinnerBattleTypes;
    private Spinner spinnerShipTypes;
    private Spinner spinnerShips;
    private Spinner spinnerCardTypes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_add_ship_drop,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.tab_drop))
                .setView(view)
                .setPositiveButton(getString(R.string.text_ok),null)
                .setNegativeButton(getString(R.string.text_cancel),((dialogInterface, i) -> onCancelClicked()));

        mapFields = new MapFieldsAdapter(this.getActivity(), R.layout.item_spinner);
        mapFields.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        battleTypes = new BattleTypesAdapter(this.getActivity(), R.layout.item_spinner);
        battleTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipTypes = new ShipTypesAdapter(this.getActivity(), R.layout.item_spinner);
        shipTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ships = new ShipsAdapter(this.getActivity(), R.layout.item_spinner);
        ships.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardTypes = new CardTypesAdapter(this.getActivity(), R.layout.item_spinner);
        cardTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        db = ((MainActivity)getActivity()).getDB();
        db.getAllMapFields().take(1).toBlocking().subscribe(
                mapFields -> {this.mapFields.clear(); this.mapFields.addAll(mapFields);});
        db.getAllBattleTypes().take(1).toBlocking().subscribe(
                battleTypes -> {this.battleTypes.clear(); this.battleTypes.addAll(battleTypes);});
        db.getAllShipTypes().take(1).toBlocking().subscribe(
                shipTypes -> {this.shipTypes.clear(); this.shipTypes.addAll(shipTypes);});
        if (shipTypes != null && shipTypes.getCount() > 1) {
            db.getShips(shipTypes.getItem(0),null).take(1).toBlocking().subscribe(
                    ships -> {
                        this.ships.clear();
                        this.ships.addAll(ships);
                    });
        } else {
            db.getAllShipsSorted().take(1).toBlocking().subscribe(
                    ships -> {
                        this.ships.clear();
                        this.ships.addAll(ships);
                    });
        }
        db.getAllCardTypes().take(1).toBlocking().subscribe(
                cardTypes -> {this.cardTypes.clear(); this.cardTypes.addAll(cardTypes);});

        spinnerMapFields = (Spinner) view.findViewById(R.id.spinner_map_field);
        spinnerMapFields.setAdapter(mapFields);
        spinnerBattleTypes = (Spinner) view.findViewById(R.id.spinner_battle_type);
        spinnerBattleTypes.setAdapter(battleTypes);
        spinnerShipTypes = (Spinner) view.findViewById(R.id.spinner_ship_type);
        spinnerShipTypes.setAdapter(shipTypes);
        spinnerShips = (Spinner) view.findViewById(R.id.spinner_ship_name);
        spinnerShips.setAdapter(ships);
        spinnerShipTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateShips();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinnerCardTypes = (Spinner) view.findViewById(R.id.spinner_card_type);
        spinnerCardTypes.setAdapter(cardTypes);

        AlertDialog dialog = builder.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view1 -> onOKClicked());

        return dialog;
    }

    public void updateShips(){
        ShipType shipType = (ShipType) spinnerShipTypes.getSelectedItem();
        db.getShips(shipType,null).take(1).toBlocking().subscribe(
                ships -> {
                    this.ships.clear();
                    this.ships.addAll(ships);
                });
    }


    public void onOKClicked(){
        long mapFieldId = ((MapField)spinnerMapFields.getSelectedItem()).getId();
        long battleTypeId = ((BattleType)spinnerBattleTypes.getSelectedItem()).getId();
        long shipId = ((Ship)spinnerShips.getSelectedItem()).getId();
        long cardTypeId = ((CardType)spinnerCardTypes.getSelectedItem()).getId();

        SubMap subMap = db.getSubMap(mapFieldId,battleTypeId).take(1).toBlocking().first();
        if (subMap == null) {
            Log.e(TAG,"Failed to add drop: : No such submap ("+mapFieldId+","+battleTypeId+")");
            Toast.makeText(this.getActivity(),"Error: "+getString(R.string.msg_add_drop_failed),Toast.LENGTH_LONG).show();
            return;
        } else{
            try {
                db.addShipDrop(shipId,cardTypeId,subMap.getId());
            } catch (RuntimeException e){
                Log.e(TAG,"Failed to add drop: Database Error: "+e.getMessage());
                Toast.makeText(this.getActivity(),"Error: "+getString(R.string.msg_add_drop_failed),Toast.LENGTH_LONG).show();
                return;
            }
        }

        Toast.makeText(this.getActivity(),getString(R.string.msg_add_drop_success),Toast.LENGTH_SHORT).show();
        dismiss();
    }

    public void onCancelClicked(){
        dismiss();
    }

    public static class MapFieldsAdapter extends ArrayAdapter<MapField>{
        public MapFieldsAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getIdName());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getIdName());
            return view;
        }
    }

    public static class BattleTypesAdapter extends ArrayAdapter<BattleType>{
        public BattleTypesAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getName());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getName());
            return view;
        }
    }
}