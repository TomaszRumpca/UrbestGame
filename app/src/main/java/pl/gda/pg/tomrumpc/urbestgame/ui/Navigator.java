package pl.gda.pg.tomrumpc.urbestgame.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.navigation.DataReceiver;
import pl.gda.pg.tomrumpc.urbestgame.navigation.Drawer;
import pl.gda.pg.tomrumpc.urbestgame.navigation.NavigatorService;

public class Navigator extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        Drawer mView = (Drawer) findViewById(R.id.drawer);
        DataReceiver dr = new DataReceiver(mView);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(dr, new IntentFilter(NavigatorService.NAVIGATION_SERVICE));

        Intent service = new Intent(this, NavigatorService.class);
        startService(service);
    }

}
