package jimmysharp.kanaclogger.presenter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.sqlbrite2.BriteDatabase;

import java.io.IOException;

import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.DatabaseInitializer;
import jimmysharp.kanaclogger.model.DatabaseManager;

public class MainActivity extends AppCompatActivity {
    DatabaseInitializer initializer = null;
    DatabaseManager dbmanager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializer = new DatabaseInitializer(this.getApplicationContext());

        try {
            BriteDatabase db = initializer.open();
            dbmanager = new DatabaseManager(db);
        } catch (IOException e) {
            this.finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_main);
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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if (initializer != null) {
            initializer.close();
            initializer.dispose();
            initializer = null;
        }
        super.onStop();
    }

    public DatabaseManager getDB(){
        return dbmanager;
    }
}
