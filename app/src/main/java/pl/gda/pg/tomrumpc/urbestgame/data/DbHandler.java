package pl.gda.pg.tomrumpc.urbestgame.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.google.common.base.Joiner;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;

import java.util.Map;

public class DbHandler {

    private final Context context;
    private SQLiteDatabase db;
    private DatabaseHelper myDbHelper;


    String taskIdSelection = Joiner.on(" ")
            .join(DbConstans.KEY_TASK_REF_ID, "=", "( SELECT", DbConstans.KEY_TASK_ID, "FROM",
                    DbConstans.TASKS_TABLE, "WHERE", DbConstans.KEY_TASK, "=? )");


    public DbHandler(Context _context) {
        context = _context;
        myDbHelper = new DatabaseHelper(context, DbConstans.DB_NAME, null, DbConstans.DB_VERSION);
    }

    public DbHandler open() {
        db = myDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public void clearDatabase() {
        db.delete(DbConstans.TASKS_TABLE, "1=1", null);
    }

    public long saveTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(DbConstans.KEY_TASK_ID, String.valueOf(task.getTaskId()));
        cv.put(DbConstans.KEY_TASK, task.getTaskName());
        cv.put(DbConstans.KEY_TASK_DESCRIPTION, task.getTaskDescription());
        cv.put(DbConstans.KEY_TASK_TYPE, 0);
        cv.put(DbConstans.KEY_ACHIEVED_POINTS, 0);
        cv.put(DbConstans.KEY_DATE_OF_COMPLETION, "");
        cv.put(DbConstans.KEY_DATE_OF_ACTIVATION, "");
        cv.put(DbConstans.KEY_LATITUDE, task.getLatitude());
        cv.put(DbConstans.KEY_LONGITUDE, task.getLongitude());
        cv.put(DbConstans.KEY_MAX_POINTS, task.getMaxPoints());
        cv.put(DbConstans.KEY_TASK_GROUP, 1);
        cv.put(DbConstans.KEY_STATE, 1);
        cv.put(DbConstans.KEY_USED_PROMPTS, 0);
        return db.insertOrThrow(DbConstans.TASKS_TABLE, null, cv);
    }

    public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection,
            String[] selectionArgs, String groupBy, String having, String sortOrder) {
        open();
        Cursor cursor = queryBuilder
                .query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    public long submitAnswer(String taskName, String answer) {

        String[] selectionArgs = {taskName};
        ContentValues cv = new ContentValues();
        cv.put(DbConstans.KEY_ANSWER, answer);
        cv.put(DbConstans.KEY_SUBMISSION_STATUS, DbConstans.SUBMISSION_STATUS_SUBMITTED);

        return db.update(DbConstans.QA_TABLE, cv, taskIdSelection, selectionArgs);
    }


    public Cursor getAnswer(String taskName) {
        String[] projection = {DbConstans.KEY_ANSWER};
        String[] selectionArgs = {taskName};
        return db.query(DbConstans.QA_TABLE, projection, taskIdSelection, selectionArgs, null, null,
                null);
    }

    public long saveAnswer(String taskName, String answer) {
        String[] selectionArgs = {taskName};
        ContentValues cv = new ContentValues();
        cv.put(DbConstans.KEY_ANSWER, answer);
        return db.update(DbConstans.QA_TABLE, cv, taskIdSelection, selectionArgs);
    }

    public Cursor getSubmissionStatus(String taskName) {
        String[] projection = {DbConstans.KEY_SUBMISSION_STATUS};
        String[] selectionArgs = {taskName};
        return db.query(DbConstans.QA_TABLE, projection, taskIdSelection, selectionArgs, null, null,
                null);
    }

    public void begin() {
        db.beginTransaction();
    }

    public void end() {
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public long updateTask(Task task) {
        String where = DbConstans.KEY_TASK_ID + " = '?'";
        String[] args = new String[]{task.getTaskName()};

        ContentValues cv = new ContentValues();
        cv.put(DbConstans.KEY_TASK_ID, String.valueOf(task.getTaskId()));
        cv.put(DbConstans.KEY_TASK, task.getTaskName());
        cv.put(DbConstans.KEY_TASK_DESCRIPTION, task.getTaskDescription());
        cv.put(DbConstans.KEY_TASK_TYPE, 0);
        cv.put(DbConstans.KEY_ACHIEVED_POINTS, 0);
        cv.put(DbConstans.KEY_DATE_OF_COMPLETION, "");
        cv.put(DbConstans.KEY_DATE_OF_ACTIVATION, "");
        cv.put(DbConstans.KEY_LATITUDE, task.getLatitude());
        cv.put(DbConstans.KEY_LONGITUDE, task.getLongitude());
        cv.put(DbConstans.KEY_MAX_POINTS, task.getMaxPoints());
        cv.put(DbConstans.KEY_TASK_GROUP, 1);
        cv.put(DbConstans.KEY_STATE, 1);
        cv.put(DbConstans.KEY_USED_PROMPTS, 0);

        return db.update(DbConstans.TASKS_TABLE, cv, where, args);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DbConstans.CREATE_MARKERS_TABLE);
            db.execSQL(DbConstans.CREATE_TASKS_TABLE);
            db.execSQL(DbConstans.CREATE_TASK_GROUPS_TABLE);
            db.execSQL(DbConstans.CREATE_QA_TABLE);

            String qstart =
                    "INSERT INTO " + DbConstans.TASK_GROUPS_TABLE + " (" + DbConstans.KEY_GROUP_ID
                            + ", " + DbConstans.KEY_GROUP_NAME + ", " + DbConstans.KEY_GROUP_COLOR
                            + ") VALUES (";

            String queryEnd = ");";

            String gvalue1 = "'1','zabianka','#AA4444'";
            String gvalue2 = "'2','przymorze','#44AA44'";
            String gvalue3 = "'3','zaspa','#4444AA'";

            String gquery1 = qstart + gvalue1 + queryEnd;
            String gquery2 = qstart + gvalue2 + queryEnd;
            String gquery3 = qstart + gvalue3 + queryEnd;

            String qaInsert1 = Joiner.on(" ")
                    .join("insert into", DbConstans.QA_TABLE, "(", DbConstans.KEY_QA_ID, ",",
                            DbConstans.KEY_TASK_REF_ID, ",", DbConstans.KEY_QUESTION, ",",
                            DbConstans.KEY_ANSWER, ") values (1, 1, 'ile masz lat?','6');");

            db.execSQL(qaInsert1);

            db.execSQL(gquery1);
            db.execSQL(gquery2);
            db.execSQL(gquery3);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVer, int newVer) {
            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.MARKER_TABLE);
            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.TASKS_TABLE);
            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.TASK_GROUPS_TABLE);

            onCreate(_db);

            Log.w("DatabaaseAdapter", "Aktualizacja bazy z wersji: " + oldVer + " do: " + newVer
                    + ". Wszystkie dane utracone.");
        }
    }
}