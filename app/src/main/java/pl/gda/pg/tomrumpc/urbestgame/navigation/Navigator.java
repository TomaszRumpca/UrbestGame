package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.gda.pg.tomrumpc.urbestgame.R;

public class Navigator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        Drawer mView = (Drawer) findViewById(R.id.drawer);
        DataReceiver dr = new DataReceiver(mView);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                dr, new IntentFilter(NavigatorService.NAVIGATION_SERVICE));

        Intent service = new Intent(this, NavigatorService.class);
        startService(service);
    }

}
