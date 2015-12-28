package pl.gda.pg.tomrumpc.urbestgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.tomrumpc.urbestgame.Constans;
import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.Task;
import pl.gda.pg.tomrumpc.urbestgame.adapter.TasksAdapter;
import pl.gda.pg.tomrumpc.urbestgame.db.DbFacade;
import pl.gda.pg.tomrumpc.urbestgame.task.QATask;

public class TasksViewer extends Activity implements OnItemClickListener {

     static public String TAG = "TasksViewer";

    private ListView tasksList;

    TextView doneTasksTV;
    String activeTaskGroup;
    Map<String, TextView> taskGroups = new HashMap<>();
    DbFacade db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DbFacade(getApplicationContext());

        setContentView(R.layout.activity_tasks_viewer);

        final List<String> groupNames = db.getTaskGroupNames();

        tasksList = (ListView) findViewById(R.id.list);

        tasksList.setOnItemClickListener(this);
        doneTasksTV = (TextView) findViewById(R.id.tasksdone);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView clickedView = (TextView) v;
                final String activeTaskGroup = clickedView.getText().toString();
                reloadList(activeTaskGroup);
            }
        };

        LinearLayout groups = (LinearLayout) findViewById(R.id.taskGroups);

        taskGroups.put(Constans.DEFAULT_TASK_GROUP_NAME, (TextView) findViewById(R.id.groupAll));
        taskGroups.get(Constans.DEFAULT_TASK_GROUP_NAME).setOnClickListener(onClickListener);

        for (String name : groupNames) {
            TextView groupTab = (TextView) getLayoutInflater().inflate(R.layout.template_task_group, null);
            groupTab.setText(name);
            groupTab.setOnClickListener(onClickListener);
            taskGroups.put(name, groupTab);
            groups.addView(groupTab);
        }
    }

    private void reloadList(String activeTaskGroup) {
        for (Map.Entry entry : taskGroups.entrySet()) {
            int styleId;
            if (activeTaskGroup.equals(entry.getKey())) {
                styleId = R.style.districtTextStyleActive;
            } else {
                styleId = R.style.districtTextStyle;
            }
            ((TextView) entry.getValue()).setTextAppearance(getApplicationContext(), styleId);
        }
        TasksAdapter adapter = new TasksAdapter(getApplicationContext(), activeTaskGroup);
        tasksList.setAdapter(adapter);
        doneTasksTV.setText(db.getTasksCompletionStatus(activeTaskGroup, tasksList.getCount()));
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        activeTaskGroup = sharedPreferences.getString("activeTaskGroup", Constans.DEFAULT_TASK_GROUP_NAME);
    }

    public void SavePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("activeTaskGroup", activeTaskGroup);
        editor.commit();
    }

    @Override
    protected void onPause() {
        SavePreferences();
        super.onPause();
    }

    @Override
    protected void onResume() {
        LoadPreferences();
        reloadList(activeTaskGroup);
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TasksAdapter.ViewHolder holder = (TasksAdapter.ViewHolder) view.getTag();
        String title = holder.title.getText().toString();

        if(db.isTaskActive(title)){
            Intent intent = new Intent(getApplicationContext(), QATask.class);
            Bundle bundle = new Bundle();
            Task task = db.getTask(title);
            bundle.putString(getResources().getString(R.string.task_title),title);
            bundle.putString(getResources().getString(R.string.task_points),String.valueOf(task.getMaxPoints()));
            bundle.putString(getResources().getString(R.string.task_description),"description");
            bundle.putString(getResources().getString(R.string.task_abbr),"xxx");
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

}
