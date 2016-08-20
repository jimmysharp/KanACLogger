package jimmysharp.kanaclogger.presenter;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate delegate;
        delegate = AppCompatDelegate.create(this,null);

        ActionBar actionBar = delegate.getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
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
