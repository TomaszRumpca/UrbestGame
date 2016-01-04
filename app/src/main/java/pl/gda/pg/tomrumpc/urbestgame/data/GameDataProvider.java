package pl.gda.pg.tomrumpc.urbestgame.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by torumpca on 2016-01-02.
 */
public class GameDataProvider extends ContentProvider {

    public static final String AUTHORITY = "pl.gda.pg.tomrumpc.urbestgame.provider";


    private static UriMatcher uriMatcher;
    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;
    private static final int GROUPS = 3;
    private static final int GROUPS_ID = 4;
    DbHandler db;


    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "tasks", TASKS);
        uriMatcher.addURI(AUTHORITY, "tasks/#", TASKS_ID);
        uriMatcher.addURI(AUTHORITY, "groups", GROUPS);
        uriMatcher.addURI(AUTHORITY, "groups/#", GROUPS_ID);
    }

    @Override
    public boolean onCreate() {
        db = new DbHandler(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        switch(uriMatcher.match(uri)){
            case TASKS:
            String tables = Joiner.on(" ").join(DbConstans.TASKS_TABLE, "LEFT OUTER JOIN",
                        DbConstans.TASK_GROUPS_TABLE, "ON", DbConstans.KEY_TASK_GROUP, "=",
                        DbConstans.KEY_GROUP_ID);

            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(tables);
            queryBuilder.setProjectionMap(getTasksProjectionMap());

            return db.query(queryBuilder, projection, selection, selectionArgs, null, null,
                        sortOrder);
            case TASKS_ID:
                return null;
            case GROUPS:
                return null;
            case GROUPS_ID:
                return null;
            default:
            throw new IllegalArgumentException("No content provider uri match");
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch(uriMatcher.match(uri)){
            case TASKS:
                return "vnd.android.cursor.dir/vnd.pl.gda.pg.tomrumpc.urbestgame.provider.task";
            case TASKS_ID:
                return "vnd.android.cursor.item/vnd.pl.gda.pg.tomrumpc.urbestgame.provider.task";
            case GROUPS:
                return "vnd.android.cursor.dir/vnd.pl.gda.pg.tomrumpc.urbestgame.provider.group";
            case GROUPS_ID:
                return "vnd.android.cursor.item/vnd.pl.gda.pg.tomrumpc.urbestgame.provider.group";
            default:
            throw new IllegalArgumentException("No content provider uri match");
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private Map<String, String> getTasksProjectionMap(){
        final Map<String, String> projectionMap = new HashMap<>();
        //FROM TASKS TABLE
        projectionMap.put(DbConstans.KEY_TASK_ID, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_TASK_ID);
        projectionMap.put(DbConstans.KEY_TASK, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_TASK);
        projectionMap.put(DbConstans.KEY_TASK_DESCRIPTION, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_TASK_DESCRIPTION);
        projectionMap.put(DbConstans.KEY_MAX_POINTS, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_MAX_POINTS);
        projectionMap.put(DbConstans.KEY_ACHIEVED_POINTS, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_ACHIEVED_POINTS);
        projectionMap.put(DbConstans.KEY_USED_PROMPTS, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_USED_PROMPTS);
        projectionMap.put(DbConstans.KEY_TASK_TYPE, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_TASK_TYPE);
        projectionMap.put(DbConstans.KEY_LATITUDE, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_LATITUDE);
        projectionMap.put(DbConstans.KEY_LONGITUDE, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_LONGITUDE);
        projectionMap.put(DbConstans.KEY_STATE, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_STATE);
        projectionMap.put(DbConstans.KEY_DATE_OF_ACTIVATION, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_DATE_OF_ACTIVATION);
        projectionMap.put(DbConstans.KEY_DATE_OF_COMPLETION, DbConstans.TASKS_TABLE + "." + DbConstans.KEY_DATE_OF_COMPLETION);
        //FROM GROUPS TABLE
        projectionMap.put(DbConstans.KEY_GROUP_NAME, DbConstans.TASK_GROUPS_TABLE + "." + DbConstans.KEY_GROUP_NAME);
        projectionMap.put(DbConstans.KEY_GROUP_ID, DbConstans.TASK_GROUPS_TABLE + "." + DbConstans.KEY_GROUP_ID);
        projectionMap.put(DbConstans.KEY_GROUP_COLOR, DbConstans.TASK_GROUPS_TABLE + "." + DbConstans.KEY_GROUP_COLOR);

        return projectionMap;
    }
}
