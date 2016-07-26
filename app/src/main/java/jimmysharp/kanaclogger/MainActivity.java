package jimmysharp.kanaclogger;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.squareup.sqlbrite.BriteDatabase;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    DatabaseInitializer dbmanager = null;
    BriteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbmanager = new DatabaseInitializer(this.getApplicationContext());

        try {
            db = dbmanager.open();
        } catch (IOException e) {
            this.finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_main, new ContentsFragment());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbmanager.close();
        dbmanager.dispose();
        dbmanager = null;
    }

    public BriteDatabase getDB(){
        return db;
    }
}
