package jimmysharp.kanaclogger.presenter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;
import jimmysharp.kanaclogger.model.table.Card;
import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.Ship;
import jimmysharp.kanaclogger.model.table.ShipType;
import rx.Subscription;

public class AddShipConstructionDialog extends DialogFragment {
    private String TAG = AddShipConstructionDialog.class.getSimpleName();

    private ShipTypesAdapter shipTypes = null;
    private ShipsAdapter ships = null;
    private CardTypesAdapter cardTypes = null;
    private DatabaseManager db = null;
    private AddShipConstructionListener listener;

    private EditText textFuel;
    private EditText textBullet;
    private EditText textSteel;
    private EditText textBauxite;

    private Spinner spinnerShipTypes;
    private Spinner spinnerShips;
    private Spinner spinnerCardTypes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_add_ship_construction,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.tab_construction))
                .setView(view)
                .setPositiveButton(getString(R.string.text_ok),null)
                .setNegativeButton(getString(R.string.text_cancel),((dialogInterface, i) -> onCancelClicked()));

        textFuel = (EditText)view.findViewById(R.id.editText_fuel);
        setResourceCheck(textFuel,
                (TextInputLayout) view.findViewById(R.id.til_fuel));
        textBullet = (EditText)view.findViewById(R.id.editText_bullet);
        setResourceCheck(textBullet,
                (TextInputLayout) view.findViewById(R.id.til_bullet));
        textSteel = (EditText)view.findViewById(R.id.editText_steel);
        setResourceCheck(textSteel,
                (TextInputLayout) view.findViewById(R.id.til_steel));
        textBauxite = (EditText)view.findViewById(R.id.editText_bauxite);
        setResourceCheck(textBauxite,
                (TextInputLayout) view.findViewById(R.id.til_bauxite));

        shipTypes = new ShipTypesAdapter(this.getActivity(), R.layout.item_spinner);
        shipTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ships = new ShipsAdapter(this.getActivity(), R.layout.item_spinner);
        ships.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardTypes = new CardTypesAdapter(this.getActivity(), R.layout.item_spinner);
        cardTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        db = ((MainActivity)getActivity()).getDB();
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

        listener = (AddShipConstructionListener) getTargetFragment();

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
        int fuel,bullet,steel,bauxite;
        long shipId,cardTypeId;
        Card card;

        try {
            fuel = Integer.parseInt(textFuel.getText().toString());
            bullet = Integer.parseInt(textBullet.getText().toString());
            steel = Integer.parseInt(textSteel.getText().toString());
            bauxite = Integer.parseInt(textBauxite.getText().toString());
            shipId = ((Ship) spinnerShips.getSelectedItem()).getId();
            cardTypeId = ((CardType) spinnerCardTypes.getSelectedItem()).getId();
        } catch (NumberFormatException e) {
            Log.e(TAG,"Failed to add construction: invalid values");
            Toast.makeText(this.getActivity(),"Error: "+getString(R.string.msg_add_construction_failed),Toast.LENGTH_LONG).show();
            return;
        }

        if (!checkResource(fuel) ||
                !checkResource(bullet) ||
                !checkResource(steel) ||
                !checkResource(bauxite)){
            Log.e(TAG,"Failed to add construction: values out of bounds");
            Toast.makeText(this.getActivity(),"Error: "+getString(R.string.msg_add_construction_failed),Toast.LENGTH_LONG).show();
            return;
        }

        try {
            card = db.getCard(shipId,cardTypeId);
            if (card == null) new RuntimeException("No such card: shipId="+shipId+",cardTypeId="+cardTypeId);
        } catch (RuntimeException e){
            Log.e(TAG,"Failed to add construction: Database Error: "+e.getMessage());
            Toast.makeText(this.getActivity(),"Error: "+getString(R.string.msg_add_construction_failed),Toast.LENGTH_LONG).show();
            return;
        }

        try {
            listener.onAddConstruction(new NewConstruction(card,fuel,bullet,steel,bauxite));
        } catch (RuntimeException e){
            Log.e(TAG,"Failed to add construction: Database Error: "+e.getMessage());
            Toast.makeText(this.getActivity(),"Error: "+getString(R.string.msg_add_construction_failed),Toast.LENGTH_LONG).show();
            return;
        }
        dismiss();
    }

    public void onCancelClicked(){
        dismiss();
    }

    private boolean checkResource(int quantity){
        if (quantity >= 30 && quantity <= 999) return true;
        else return false;
    }

    private void setResourceCheck(final EditText text, final TextInputLayout til){
        text.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        try {
                            if (checkResource(Integer.parseInt(s.toString()))) {
                                til.setErrorEnabled(false);
                            } else {
                                til.setError(getString(R.string.text_invalid_resource));
                                til.setErrorEnabled(true);
                            }
                        } catch (NumberFormatException e){
                            til.setError(getString(R.string.text_invalid_resource));
                            til.setErrorEnabled(true);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {}
                }
        );
    }
}