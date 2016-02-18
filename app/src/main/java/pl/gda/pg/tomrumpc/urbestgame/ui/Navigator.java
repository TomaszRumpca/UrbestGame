package pl.gda.pg.tomrumpc.urbestgame.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import pl.gda.pg.tomrumpc.urbestgame.Constans;
import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.data.DbFacade;
import pl.gda.pg.tomrumpc.urbestgame.navigation.DataReceiver;
import pl.gda.pg.tomrumpc.urbestgame.navigation.Drawer;
import pl.gda.pg.tomrumpc.urbestgame.navigation.NavigatorService;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Navigator extends Activity implements ServiceConnection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        Drawer mView = (Drawer) findViewById(R.id.drawer);
        DataReceiver dr = new DataReceiver(mView);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(dr, new IntentFilter(NavigatorService.NAVIGATION_SERVICE));

    }

    @Override
    protected void onStart() {
        super.onStart();

        DbFacade db = new DbFacade(getApplicationContext());

        List<Task> tasks = db.getTasks(Constans.DEFAULT_TASK_GROUP_NAME);
        ArrayList<String> taskNames = new ArrayList<>(tasks.size());
        Bundle extras = new Bundle();
        for (Task task : tasks) {
            String taskName = task.getTaskName();
            extras.putSerializable(taskName, task);
            taskNames.add(taskName);
        }
        extras.putStringArrayList("tasks", taskNames);

        Intent service = new Intent(this, NavigatorService.class);
        service.putExtras(extras);
        startService(service);
        bindService(service, this, getApplicationContext().BIND_ADJUST_WITH_ACTIVITY);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
