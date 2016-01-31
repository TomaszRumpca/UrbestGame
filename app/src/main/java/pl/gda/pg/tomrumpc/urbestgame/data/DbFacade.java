package pl.gda.pg.tomrumpc.urbestgame.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import pl.gda.pg.tomrumpc.urbestgame.Constans;
import pl.gda.pg.tomrumpc.urbestgame.task.Group;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;

public class DbFacade {


    private static final String URL = "content://" + GameDataProvider.AUTHORITY + "/";
    private static final Uri CONTENT_URI_PREFIX = Uri.parse(URL);

    private static DbHandler db;
    private ContentResolver contentResolver;

    public DbFacade(Context context) {
        if (db == null) {
            db = new DbHandler(context);
            contentResolver = context.getContentResolver();
        }
        contentResolver = context.getContentResolver();
    }

    public List<String> getTaskGroupNames(){
        List<Group> groups = getTaskGroups();
        List<String> groupNames = new ArrayList<>(groups.size());
        for(Group group : groups){
            groupNames.add(group.getGroupName());
        }
        return groupNames;
    }

    public List<Group> getTaskGroups() {

        final String sortOrder = DbConstans.KEY_GROUP_ID + " ASC";

        Uri uri = Uri.parse(CONTENT_URI_PREFIX + "/groups");
        Cursor cursor = contentResolver
                .query(uri, getDefaultGroupProjection(), null, null, sortOrder);

        final List<Group> groups = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                groups.add(getGroupFromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return groups;
    }

    public Integer getCompletedTasksCount(String group) {

        int completed = 0;
        for(Task task : getTasks(group)){
            if(Task.State.DONE.equals(task.getState())){
                completed++;
            }
        }
        return completed;
    }

    public List<Task> getTasks(String groupName) {

        final String sortOrder = DbConstans.KEY_TASK_ID + " ASC";

        String selection = null;
        String[] selectionArgs = null;

        if (!Constans.DEFAULT_TASK_GROUP_NAME.equals(groupName)) {
            selection = DbConstans.KEY_GROUP_NAME + " = ?";
            selectionArgs = new String[]{groupName};
        }

        Uri uri = Uri.parse(CONTENT_URI_PREFIX + "/tasks");
        Cursor cursor = contentResolver
                .query(uri, getDefaultTaskProjection(), selection, selectionArgs, sortOrder);

        final List<Task> tasks = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tasks.add(getTaskFromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return tasks;
    }

    public Task getTask(String title) {

        String selection = DbConstans.KEY_TASK + " = ?";
        String[] selectionArgs = new String[]{title};

        Uri uri = Uri.parse(CONTENT_URI_PREFIX + "/tasks");
        Cursor cursor = contentResolver
                .query(uri, getDefaultTaskProjection(), selection, selectionArgs, null);

        Task task = null;

        if (cursor != null && cursor.moveToFirst()) {
            task = getTaskFromCursor(cursor);
            cursor.close();
        }

        return task;
    }


    private String[] getDefaultTaskProjection(){
        //@formatter:off
        return new String[]{
                DbConstans.KEY_TASK_ID,
                DbConstans.KEY_TASK,
                DbConstans.KEY_TASK_DESCRIPTION,
                DbConstans.KEY_MAX_POINTS,
                DbConstans.KEY_ACHIEVED_POINTS,
                DbConstans.KEY_USED_PROMPTS,
                DbConstans.KEY_TASK_TYPE,
                DbConstans.KEY_LATITUDE,
                DbConstans.KEY_LONGITUDE,
                DbConstans.KEY_STATE,
                DbConstans.KEY_DATE_OF_ACTIVATION,
                DbConstans.KEY_DATE_OF_COMPLETION,
                DbConstans.KEY_GROUP_NAME,
                DbConstans.KEY_GROUP_ID,
                DbConstans.KEY_GROUP_COLOR};
        //@formatter:on
    }

    private String[] getDefaultGroupProjection(){
        //@formatter:off
        return new String[]{
                DbConstans.KEY_GROUP_NAME,
                DbConstans.KEY_GROUP_ID,
                DbConstans.KEY_GROUP_COLOR};
        //@formatter:on
    }

    private Task getTaskFromCursor(Cursor cursor){
        return Task.builder()
                .taskId(cursor.getInt(cursor.getColumnIndex(DbConstans.KEY_TASK_ID)))
                .taskName(cursor.getString(cursor.getColumnIndex(DbConstans.KEY_TASK)))
                .maxPoints(cursor.getInt(cursor.getColumnIndex(DbConstans.KEY_MAX_POINTS)))
                .achivedPoints(cursor.getInt(
                        cursor.getColumnIndex(DbConstans.KEY_ACHIEVED_POINTS))).usedPrompts(
                        cursor.getInt(cursor.getColumnIndex(DbConstans.KEY_USED_PROMPTS)))
                .taskType(cursor.getInt(cursor.getColumnIndex(DbConstans.KEY_TASK_TYPE)))
                .latitude(cursor.getDouble(cursor.getColumnIndex(DbConstans.KEY_LATITUDE)))
                .longitude(
                        cursor.getDouble(cursor.getColumnIndex(DbConstans.KEY_LONGITUDE)))
                .state(cursor.getInt(cursor.getColumnIndex(DbConstans.KEY_STATE)))
                .dateOfActivation(cursor.getString(
                        cursor.getColumnIndex(DbConstans.KEY_DATE_OF_ACTIVATION)))
                .dateOfCompletion(cursor.getString(
                        cursor.getColumnIndex(DbConstans.KEY_DATE_OF_COMPLETION)))
                .groupId(cursor.getInt(cursor.getColumnIndex(DbConstans.KEY_GROUP_ID)))
                .groupName(
                        cursor.getString(cursor.getColumnIndex(DbConstans.KEY_GROUP_NAME)))
                .color(cursor.getString(cursor.getColumnIndex(DbConstans.KEY_GROUP_COLOR)))
                .build();
    }

    private Group getGroupFromCursor(Cursor cursor){
        return Group.builder()
                .groupId(cursor.getInt(cursor.getColumnIndex(DbConstans.KEY_GROUP_ID)))
                .groupName(
                        cursor.getString(cursor.getColumnIndex(DbConstans.KEY_GROUP_NAME)))
                .color(cursor.getString(cursor.getColumnIndex(DbConstans.KEY_GROUP_COLOR)))
                .build();
    }

    public boolean submitAnswer(String taskName, String answer) {

        db.open();

        long updatedRows = db.submitAnswer(taskName, answer);

        db.close();

        return updatedRows > 0L ? true : false;
    }

    public boolean isTaskActive(String taskName) {
        return getTask(taskName).isActive() ? true : false;
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
