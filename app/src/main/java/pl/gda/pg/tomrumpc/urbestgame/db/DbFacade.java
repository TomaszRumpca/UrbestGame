package pl.gda.pg.tomrumpc.urbestgame.db;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.tomrumpc.urbestgame.Constans;
import pl.gda.pg.tomrumpc.urbestgame.Task;

public class DbFacade {


    private static DbHandler db;

    public DbFacade(Context context) {
        if (db == null) {
            db = new DbHandler(context);
        }
    }

    public ArrayList<String> getTaskGroupNames() {

        ArrayList<String> names = new ArrayList<>();

        db.open();

        Cursor cursor = db.getAllEntries(DbConstans.TASK_GROUPS_TABLE);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(DbConstans.GROUP_NAME_COLUMN));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();

        return names;
    }

    public String getTasksCompletionStatus(String currentGroup, int numberOfTasksInGroup) {

        db.open();

        int count = db.getCompletedTaskNumber(currentGroup);

        db.close();

        return count + "/" + numberOfTasksInGroup;
    }

    public List<Task> getTasks(String groupName) {

        final List<Task> tasks = new ArrayList<>();

        final String[] projection = new String[]{DbConstans.KEY_TASK, DbConstans.KEY_ACHIEVED_POINTS,
                DbConstans.KEY_MAX_POINTS, DbConstans.KEY_STATE, DbConstans.KEY_GROUP_COLOR};
        final String sortOrder = DbConstans.KEY_TASK_ID + " ASC";

        final Map<String, String> projectionMap = new HashMap<>();
        projectionMap.put(DbConstans.KEY_TASK, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_TASK);
        projectionMap.put(DbConstans.KEY_ACHIEVED_POINTS, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_ACHIEVED_POINTS);
        projectionMap.put(DbConstans.KEY_MAX_POINTS, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_MAX_POINTS);
        projectionMap.put(DbConstans.KEY_STATE, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_STATE);
        projectionMap.put(DbConstans.KEY_TASK_GROUP, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_TASK_GROUP);
        projectionMap.put(DbConstans.KEY_GROUP_NAME, DbConstans.TASK_GROUPS_TABLE + "." + DbConstans.KEY_GROUP_NAME);
        projectionMap.put(DbConstans.KEY_GROUP_ID, DbConstans.TASK_GROUPS_TABLE + "." + DbConstans.KEY_GROUP_ID);
        projectionMap.put(DbConstans.KEY_GROUP_COLOR, DbConstans.TASK_GROUPS_TABLE + "." + DbConstans.KEY_GROUP_COLOR);

        String selection = null;
        String[] selectionArgs = null;

        if (!Constans.DEFAULT_TASK_GROUP_NAME.equals(groupName)) {
            selection = DbConstans.KEY_GROUP_NAME + " = ?";
            selectionArgs = new String[]{groupName};
        }

        db.open();

        final Cursor cursor = db.getAllTasks(projectionMap, projection, selection, selectionArgs, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                final Task task = new Task();
                task.setTaskName(cursor.getString(0));
                task.setAchivedPoints(cursor.getInt(1));
                task.setMaxPoints(cursor.getInt(2));
                task.setState(cursor.getInt(3));
                task.setColor(cursor.getString(4));
                tasks.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();

        return tasks;
    }

    public Task getTask(String title) {

        Task task = new Task();
        db.open();

        Cursor cursor = db.getTask(title);

        if (cursor != null && cursor.moveToFirst()) {
            task.setTaskName(cursor.getString(0));
            task.setMaxPoints(cursor.getInt(1));
            cursor.close();
        }

        db.close();
        return task;
    }

    public boolean submitAnswer(String taskName, String answer) {

        db.open();

        long updatedRows = db.submitAnswer(taskName, answer);

        db.close();

        return updatedRows > 0L ? true : false;
    }

    public boolean isTaskActive(String taskName) {

        db.open();

        Cursor cursor = db.getTaskState(taskName);

        int taskStatus = -1;
        if(cursor.moveToFirst()){
            taskStatus = cursor.getInt(0);
        }

        db.close();
        return taskStatus == Task.State.ACTIVE.getValue() ? true : false;
    }

    public String getAnswer(String taskName) {

        db.open();

        Cursor cursor = db.getAnswer(taskName);

        String answer = null;
        if(cursor.moveToFirst()){
            answer = cursor.getString(0);
        }

        db.close();

        return answer;
    }

    public boolean saveCurrentAnswer(String taskName, String answer) {

        db.open();
        long updatedRows = -1L;

        Cursor cursor = db.getSubmissionStatus(taskName);

        int submissionStatus = -1;

        if(cursor.moveToFirst()){
            submissionStatus = cursor.getInt(0);
        }
        if(submissionStatus == DbConstans.SUBMISSION_STATUS_NOT_SUBMITTED){
            updatedRows = db.saveAnswer(taskName, answer);
        }

        db.close();

        return updatedRows == 1L ? true : false;
    }
}
