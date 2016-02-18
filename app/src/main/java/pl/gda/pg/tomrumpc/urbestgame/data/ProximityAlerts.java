package pl.gda.pg.tomrumpc.urbestgame.data;

import java.util.List;
import java.util.Locale;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;

/**
 * Created by torumpca on 2016-02-18.
 */
public class ProximityAlerts {


    Context context;

    public ProximityAlerts(Context context) {
        this.context = context;
        AddProximityAlerts();

    }

    private void AddProximityAlerts() throws SecurityException {

        DbFacade db = new DbFacade(context);
        List<Task> tasks = db.getTasks("???");
        float radius = 100;


        for (Task task : tasks) {
            LocationManager lm =
                    (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            String taskName = task.getTaskName();
            String title = taskName.toLowerCase(Locale.getDefault());
            title = title.replaceAll("\\s", "_");
            String intentStr = "lemur.urbest." + title + ".ProximityAlert";

            Intent i = new Intent(intentStr);
            Bundle bundle = new Bundle();
            bundle.putString("taskName", taskName);
            i.putExtras(bundle);
            PendingIntent pi = PendingIntent.getBroadcast(context, -1, i, 0);
            lm.addProximityAlert(task.getLatitude(), task.getLongitude(), radius, -1, pi);

            IntentFilter filter = new IntentFilter(intentStr);
            context.registerReceiver(new ProximityAlertsReceiver(), filter);
            Log.i("ProximityAlert",
                    "Added : " + intentStr + " title: " + title + " taskName: " + taskName);

        }

    }

}
