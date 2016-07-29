package jimmysharp.kanaclogger.presenter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseManager;
import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.Ship;
import rx.Subscription;

public class AddShipConstructionDialog extends DialogFragment {
    private String TAG = AddShipConstructionDialog.class.getSimpleName();

    private ShipsAdapter ships = null;
    private Subscription shipsSubscription = null;
    private CardTypesAdapter cardTypes = null;
    private Subscription cardTypesSubscription = null;
    private DatabaseManager db = null;

    private EditText textFuel;
    private EditText textBullet;
    private EditText textSteel;
    private EditText textBauxite;

    private Spinner spinnerShips;
    private Spinner spinnerCardTypes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_add_ship_construction,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.tab_construction))
                .setView(view)
                .setPositiveButton(getString(R.string.text_ok),((dialogInterface, i) -> onOKClicked()))
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

        ships = new ShipsAdapter(this.getActivity(),android.R.layout.simple_spinner_item);
        ships.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardTypes = new CardTypesAdapter(this.getActivity(),android.R.layout.simple_spinner_item);
        cardTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        db = ((MainActivity)getActivity()).getDB();
        shipsSubscription = db.getAllShips().subscribe(
                ships -> {this.ships.clear(); this.ships.addAll(ships);});
        cardTypesSubscription = db.getAllCardTypes().subscribe(
                cardTypes -> {this.cardTypes.clear(); this.cardTypes.addAll(cardTypes);});

        spinnerShips = (Spinner) view.findViewById(R.id.spinner_ship_name);
        spinnerShips.setAdapter(ships);
        spinnerCardTypes = (Spinner) view.findViewById(R.id.spinner_card_type);
        spinnerCardTypes.setAdapter(cardTypes);

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shipsSubscription != null) shipsSubscription.unsubscribe();
        if (cardTypesSubscription != null) cardTypesSubscription.unsubscribe();
    }

    public void onOKClicked(){
        long fuel,bullet,steel,bauxite;
        long shipId,cardTypeId;

        try {
            fuel = Long.parseLong(textFuel.getText().toString());
            bullet = Long.parseLong(textBullet.getText().toString());
            steel = Long.parseLong(textSteel.getText().toString());
            bauxite = Long.parseLong(textBauxite.getText().toString());
            shipId = ((Ship) spinnerShips.getSelectedItem()).getId();
            cardTypeId = ((CardType) spinnerCardTypes.getSelectedItem()).getId();
        } catch (NumberFormatException e) {
            Log.e(TAG,"Failed to add construction: invalid args");
            //TODO:トースト表示
            return;
        }

        try {
            db.addShipConstruction(shipId,cardTypeId,fuel,bullet,steel,bauxite);
        } catch (RuntimeException e){
            Log.e(TAG,"Failed to add construction: Database Error: "+e.getMessage());
            //TODO:トースト表示
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
                                til.setError(getString(R.string.text_invalid_resourse));
                                til.setErrorEnabled(true);
                            }
                        } catch (NumberFormatException e){
                            til.setError(getString(R.string.text_invalid_resourse));
                            til.setErrorEnabled(true);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {}
                }
        );
    }

    public static class ShipsAdapter extends ArrayAdapter<Ship> {
        public ShipsAdapter(Context context, int resource) {
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

    public static class CardTypesAdapter extends ArrayAdapter<CardType>{
        public CardTypesAdapter(Context context, int resource) {
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