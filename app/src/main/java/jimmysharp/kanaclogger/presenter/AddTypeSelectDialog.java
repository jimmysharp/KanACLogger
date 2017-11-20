package jimmysharp.kanaclogger.presenter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import jimmysharp.kanaclogger.R;

public class AddTypeSelectDialog extends DialogFragment{
    private AddTypeSelectListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CharSequence[] items =
                {
                        getString(R.string.text_add_construction),
                        getString(R.string.text_add_drop),
                        getString(R.string.text_add_save)
                };
        listener = (AddTypeSelectListener) getTargetFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, (dialogInterface, i) -> {
            switch (i){
                case 0:
                    listener.onConstructionSelected();
                    break;
                case 1:
                    listener.onDropSelected();
                    break;
                case 2:
                    listener.onSaveSelected();
                    break;
                default:
                    break;
            }
        });

        return builder.create();
    }
}
