package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG = "setting_fragment";

    private Uri authUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate delegate;
        delegate = AppCompatDelegate.create(this,null);

        ActionBar actionBar = delegate.getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment(),FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.v(TAG,"onNewIntent");
        if (intent == null
                || intent.getData() == null
                || !intent.getData().toString().startsWith("kanactwit://oauth")) {
            return;
        }
        this.authUri = intent.getData();
        Log.v(TAG,"New auth Uri get");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (authUri != null){
            Log.v(TAG,"call SettingFragment for put access token");
            Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (fragment != null && fragment instanceof SettingsFragment)
                ((SettingsFragment) fragment).setAccessToken(authUri);
            authUri = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
