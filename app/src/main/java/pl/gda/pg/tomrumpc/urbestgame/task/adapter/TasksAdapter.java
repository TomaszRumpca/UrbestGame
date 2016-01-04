package pl.gda.pg.tomrumpc.urbestgame.task.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;
import pl.gda.pg.tomrumpc.urbestgame.data.DbConstans;
import pl.gda.pg.tomrumpc.urbestgame.data.DbFacade;

import static pl.gda.pg.tomrumpc.urbestgame.task.Task.State.ACTIVE;

public class TasksAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    String groupName;
    int itemCount = 0;
    List<Task> tasks;
    private int[] backgroundColors = {0xFFf2f2f2, 0xFFe8e8e8};


    public TasksAdapter(Context context, String groupId) {
        this.groupName = groupId;
        this.mInflater = LayoutInflater.from(context);
        DbFacade db = new DbFacade(context);
        tasks = db.getTasks(groupName);
        itemCount = tasks.size();
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public Object getItem(int arg0) {
        return tasks.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_schema_task2, null);
        }

        Task cTask = tasks.get(position);

        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) convertView.findViewById(R.id.task_title);
        holder.points = (TextView) convertView.findViewById(R.id.points);
        holder.icon = convertView.findViewById(R.id.group_icon);
        holder.checkedIcon = (ImageView) convertView.findViewById(R.id.checked_icon);
        holder.lockIcon = (ImageView) convertView.findViewById(R.id.lockIcon);

        holder.title.setText(cTask.getTaskName());
        holder.points.setText(String.valueOf(cTask.getMaxPoints()));
        holder.icon.setBackgroundColor(Color.parseColor(cTask.getColor()));

        switch (cTask.getState()) {
            case ACTIVE:
                holder.lockIcon.setImageResource(R.drawable.unlock);
                break;
            case WAITING_FOR_APPROVAL:
                holder.lockIcon.setImageResource(R.drawable.question);
                break;
            case LOCKED:
                holder.lockIcon.setImageResource(R.drawable.lock);
                break;
            case DONE:
                holder.checkedIcon.setVisibility(View.VISIBLE);
                holder.lockIcon.setImageResource(R.drawable.lock);
                break;
            default:
                break;
        }

        convertView.setTag(holder);

        int posColor = position % backgroundColors.length;
        convertView.setBackgroundColor(this.backgroundColors[posColor]);

        return convertView;
    }

    public class ViewHolder {
        View icon;
        ImageView checkedIcon;
        ImageView lockIcon;
        public TextView title;
        TextView points;
    }
}
    
