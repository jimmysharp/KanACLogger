package jimmysharp.kanaclogger.presenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import jimmysharp.kanaclogger.R;

public class LicenseDialog extends DialogFragment {
    private Dialog licenseDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (licenseDialog != null) return licenseDialog;
        View view = View.inflate(getActivity(), R.layout.fragment_license,null);
        WebView webView = view.findViewById(R.id.webview_license);
        webView.loadUrl(getString(R.string.path_license));

        licenseDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.text_license))
                .setView(view)
                .setPositiveButton(R.string.text_ok,null)
                .create();

        return licenseDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        licenseDialog = null;
    }
}
