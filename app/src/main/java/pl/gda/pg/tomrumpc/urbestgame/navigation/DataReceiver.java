package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class DataReceiver extends BroadcastReceiver {

    onPositionChangedListener receiver;

    public DataReceiver(onPositionChangedListener receiver){
        this.receiver = receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        double[] b = intent.getDoubleArrayExtra("bearingB");
        receiver.onPositionChanged(b);
    }

}
