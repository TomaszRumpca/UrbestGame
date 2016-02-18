package pl.gda.pg.tomrumpc.urbestgame.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;
import pl.gda.pg.tomrumpc.urbestgame.ui.Map;

/**
 * Created by torumpca on 2016-02-18.
 */
public class ProximityAlertsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent i) {

        String k = LocationManager.KEY_PROXIMITY_ENTERING;

        boolean state = i.getBooleanExtra(k, false);

        String taskName = i.getStringExtra("taskName");

        Log.i("ProximityAlertReceiver", "OnReceive: " + taskName + ", state: " + k);

        DbFacade db = new DbFacade(context);
        Task task = db.getTask(taskName);
        boolean undiscovered = !task.isActive();


        if (state && taskName != null && undiscovered) {
            createNotification(context, taskName);
            db.updateTask(task);
        }

    }

    public void createNotification(Context context, String taskName) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(context, Map.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new NotificationCompat.Builder(context).setContentTitle(taskName)
                .setContentText("Nowe zadanie aktywne!").setSmallIcon(R.drawable.iko)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
        Log.i("ProximityAlert", "new notification");
    }

}
