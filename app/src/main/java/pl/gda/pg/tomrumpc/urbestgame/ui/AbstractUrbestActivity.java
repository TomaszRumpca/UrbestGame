package pl.gda.pg.tomrumpc.urbestgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;

import pl.gda.pg.tomrumpc.urbestgame.R;

/**
 * Created by torumpca on 2016-01-02.
 */
public abstract class AbstractUrbestActivity extends AppCompatActivity {

    protected ViewStub stub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stub = (ViewStub) findViewById(R.id.content_stub);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_map:
                startActivity(new Intent(this, Map.class));
                return true;
            case R.id.menu_tasks_list:
                startActivity(new Intent(this, TasksViewer.class));
                return true;
            case R.id.menu_navigator:
                startActivity(new Intent(this, Navigator.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
