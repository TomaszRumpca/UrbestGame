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

import java.util.Map;

public class DbHandler {

    private final Context context;
    private SQLiteDatabase db;
    private DatabaseHelper myDbHelper;


    String taskIdSelection =
            Joiner.on(" ").join(DbConstans.KEY_TASK_REF_ID, "=",
            "( SELECT", DbConstans.KEY_TASK_ID, "FROM", DbConstans.TASKS_TABLE,
            "WHERE", DbConstans.KEY_TASK, "=? )");


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

//    public void deleteAll() {
//        db.delete(DbConstans.LOCATIONS_TABLE, "1", null);
//        db.delete(DbConstans.MARKER_TABLE, "1", null);
//        db.delete(DbConstans.TASKS_TABLE, "1", null);
//    }

//    public Cursor getAllEntries(String fromTable, String[] projection, String sortOrder) {
//       return db.query(fromTable, projection, null, null, null, null, sortOrder);
//    }

    public Cursor getAllEntries(String fromTable) {

//        if (fromTable.equals(DbConstans.LOCATIONS_TABLE)) {
//            String[] columns = {DbConstans.KEY_LATITUDE, DbConstans.KEY_LONGITUDE, DbConstans.KEY_DATE};
//            return db.query(DbConstans.LOCATIONS_TABLE, columns, null, null, null, null,
//                    DbConstans.KEY_DATE);
//        } else
        if (fromTable.equals(DbConstans.MARKER_TABLE)) {
            String[] columns = {DbConstans.KEY_MARKER_ID, DbConstans.KEY_MARKER_LATITUDE,
                    DbConstans.KEY_MARKER_LONGITUDE, DbConstans.KEY_MARKER_TASK, DbConstans.KEY_MARKER_DRAGGABLE};
            return db.query(DbConstans.MARKER_TABLE, columns, null, null, null, null,
                    DbConstans.KEY_MARKER_ID);
        } else if (fromTable.equals(DbConstans.TASKS_TABLE)) {
            return db.query(DbConstans.TASKS_TABLE, null, null, null, null, null,
                    DbConstans.KEY_TASK_ID);
        } else if (fromTable.equals(DbConstans.TASK_GROUPS_TABLE)) {
            return db.query(DbConstans.TASK_GROUPS_TABLE, null, null, null, null, null,
                    DbConstans.KEY_GROUP_ID);
        } else {
            return null;
        }

    }

    public Cursor getAllTasks(Map<String, String> projectionMap, String[] projection, String selection,
                              String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DbConstans.TASKS_TABLE +
                " LEFT OUTER JOIN " + DbConstans.TASK_GROUPS_TABLE + " ON " +
                DbConstans.KEY_TASK_GROUP + " = " + DbConstans.KEY_GROUP_ID);
        queryBuilder.setProjectionMap(projectionMap);

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public int getCompletedTaskNumber(String groupName) {
        String[] projection = {DbConstans.KEY_ACHIEVED_POINTS};


        return 0;//TODO
    }

//    public int getDoneTaskCount(float district) {
//
//        String[] columns = {DbConstans.KEY_ACHIEVED_POINTS};
//        Cursor cursor = null;
//
//        if (district != -1) {
//            String selection = DbConstans.KEY_TASK_GROUP + " = '" + district + "'";
//
//            cursor = db.query(DbConstans.TASKS_TABLE, columns, selection, null, null,
//                    null, null);
//        } else {
//            cursor = db.query(DbConstans.TASKS_TABLE, columns, null, null, null,
//                    null, null);
//        }
//
//        int count = 0;
//        if (cursor.moveToFirst()) {
//
//            do {
//                if (cursor.getInt(0) > -1) {
//                    count++;
//                }
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return count;
//    }


    public long updateTaskAsActive(int taskId) {
        return activateTask(DbConstans.KEY_TASK_ID + " ='" + taskId + "'");
    }

    public long updateTaskAsActive(String taskName) {
        return activateTask(DbConstans.KEY_TASK + " ='" + taskName + "'");
    }

    private long activateTask(String where) {
        ContentValues cv = new ContentValues();
        cv.put(DbConstans.KEY_STATE, 1);
        return db.update(DbConstans.TASKS_TABLE, cv, where, null);
    }


    public boolean isTaskActive(int taskId) {
        return queryIfTaskActive(DbConstans.KEY_TASK_ID + " ='" + taskId + "'");
    }

    public boolean isTaskActive(String taskName) {
        return queryIfTaskActive(DbConstans.KEY_TASK + " ='" + taskName + "'");
    }

    private boolean queryIfTaskActive(String selection) {
        boolean active = false;

        Cursor cursor = db.query(DbConstans.TASKS_TABLE,
                new String[]{DbConstans.KEY_STATE}, selection, null, null, null, null);

        if (cursor.moveToFirst()) {
            int i = cursor.getInt(0);
            if (i == 0) {
                active = true;
            }
        }
        return active;

    }

    public Cursor getTask(String title) {

        String[] projection = {DbConstans.KEY_TASK, DbConstans.KEY_MAX_POINTS};

        String selection = DbConstans.KEY_TASK + " = '" + title + "'";

        return db.query(DbConstans.TASKS_TABLE, projection, selection, null, null, null, null);
    }

//    public Cursor getTaskID(String taskName) {
//        String[] projection = {DbConstans.KEY_TASK_ID};
//        String selection = Joiner.on(" ").join(DbConstans.KEY_TASK, "=", taskName);
//        return db.query(DbConstans.TASKS_TABLE, projection, selection, null, null, null, null);
//    }

    public long submitAnswer(String taskName, String answer) {

        String[] selectionArgs = {taskName};
        ContentValues cv = new ContentValues();
        cv.put(DbConstans.KEY_ANSWER, answer);
        cv.put(DbConstans.KEY_SUBMISSION_STATUS, DbConstans.SUBMISSION_STATUS_SUBMITTED);

        return db.update(DbConstans.QA_TABLE, cv, taskIdSelection, selectionArgs);
    }

    public Cursor getTaskState(String taskName) {
        String task = new StringBuilder("'").append(taskName).append("'").toString();
        String[] projection = {DbConstans.KEY_STATE};
        String selection = Joiner.on(" ").join(DbConstans.KEY_TASK, "=", task);
        return db.query(DbConstans.TASKS_TABLE, projection, selection, null, null, null, null);
    }

    public Cursor getAnswer(String taskName) {
        String[] projection = {DbConstans.KEY_ANSWER};
        String[] selectionArgs = {taskName};
        return db.query(DbConstans.QA_TABLE, projection, taskIdSelection, selectionArgs,
                null, null, null);
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
        return db.query(DbConstans.QA_TABLE, projection, taskIdSelection, selectionArgs,
                null, null, null);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            db.execSQL(DbConstans.CREATE_LOCATION_TABLE);
            db.execSQL(DbConstans.CREATE_MARKERS_TABLE);
            db.execSQL(DbConstans.CREATE_ALERTS_TABLE);
            db.execSQL(DbConstans.CREATE_TASKS_TABLE);
            db.execSQL(DbConstans.CREATE_TASK_GROUPS_TABLE);
            db.execSQL(DbConstans.CREATE_QA_TABLE);

            String zabianka = "1";
            String przymorze = "2";
            String zaspa = "3";

            String qstart = "INSERT INTO " + DbConstans.TASK_GROUPS_TABLE + " ("
                    + DbConstans.KEY_GROUP_ID + ", " + DbConstans.KEY_GROUP_NAME + ", " + DbConstans.KEY_GROUP_COLOR + ") VALUES (";

            String queryStart = "INSERT INTO " + DbConstans.TASKS_TABLE + " ("
                    + DbConstans.KEY_TASK_ID + ", " + DbConstans.KEY_TASK + ", " + DbConstans.KEY_MAX_POINTS
                    + ", " + DbConstans.KEY_ACHIEVED_POINTS + ", " + DbConstans.KEY_USED_PROMPTS
                    + ", " + DbConstans.KEY_TASK_GROUP + ", " + DbConstans.KEY_LATITUDE + ", " + DbConstans.KEY_LONGITUDE + ", "
                    + DbConstans.KEY_STATE + ", " + DbConstans.KEY_DATE_OF_ACTIVATION + ", " + DbConstans.KEY_DATE_OF_COMPLETION
                    + ") VALUES (";
            String queryEnd = ");";

            String values1 = "'" + DbConstans.INVESTMENT_AREA + "', 'Warzywniak', '6','-1','null', '" + zaspa + "', '54.396575', '18.585299', '1', 'null', 'null'";
            String values2 = "'" + DbConstans.SCULPTURES + "', 'Sztuka w przestrzeni', '5','-1','null', '" + zabianka + "', '54.420478', '18.57427', '1', 'null', 'null'";
            String values3 = "'" + DbConstans.RIDDLE + "', 'Kładka', '4','-1','null', '" + zaspa + "', '54.394339', '18.601804', '0', 'null', 'null'";
            String values4 = "'" + DbConstans.FOUNTAIN + "', 'Połamany Herbatnik', '4','-1','null', '" + zabianka + "', '54.420540', '18.575585', '0', 'null', 'null'";
            String values5 = "'" + DbConstans.PLAYING_FIELD + "', 'Piłka w grze!', '10','-1','null', '" + zabianka + "', '54.420202', '18.583375', '0', 'null', 'null'";
            String values6 = "'" + DbConstans.ERGO + "', 'Ergo Arena', '5','-1','null', '" + zabianka + "', '54.424478', '18.579850', '0', 'null', 'null'";
            String values7 = "'" + DbConstans.LAKE + "', 'Jezioro', '5','-1','null', '" + zabianka + "', '54.416325', '18.573062', '0', 'null', 'null'";
            String values8 = "'" + DbConstans.BLOCK + "', 'Blokowisko', '10','-1','null', '" + przymorze + "', '54.407531', '18.597417', '0', 'null', 'null'";
            String values9 = "'" + DbConstans.BAKERY + "', 'Piekarnia Graczyk', '2','-1','null', '" + przymorze + "', '54.401828', '18.579293', '0', 'null', 'null'";
            String values10 = "'" + DbConstans.CROSSWORD + "', 'Dywizjonu 303', '7','-1','null', '" + zaspa + "', '54.390293', '18.599415', '0', 'null', 'null'";
            String values11 = "'" + DbConstans.OLDAIRPORT + "', 'Dziedzictwo dzielnicy', '3','-1','null', '" + zaspa + "', '54.399636', '18.606868', '0', 'null', 'null'";
            String values12 = "'" + DbConstans.POPE + "', 'Papież Polak', '5','-1','null', '" + zaspa + "', '54.399196', '18.595927', '0', 'null', 'null'";
            String values13 = "'" + DbConstans.FALOWIEC + "', 'Falowiec', '5','-1','null', '" + przymorze + "', '54.415966', '18.586301', '0', 'null', 'null'";
            String values14 = "'" + DbConstans.BALTIC + "', 'Bałtyk', '6','-1','null', '" + przymorze + "', '54.409423', '18.575863', '0', 'null', 'null'";

            //                TASK_ID,      TASK_NAME,    MAX_Points, AchivedPoints, used prompts, group, lat', 'lng, isActive, dateofActivation, dateOfcompletion
            //															init -1

            String gvalue1 = "'1','zabianka','#AA4444'";
            String gvalue2 = "'2','przymorze','#44AA44'";
            String gvalue3 = "'3','zaspa','#4444AA'";

            String gquery1 = qstart + gvalue1 + queryEnd;
            String gquery2 = qstart + gvalue2 + queryEnd;
            String gquery3 = qstart + gvalue3 + queryEnd;

            String query1 = queryStart + values1 + queryEnd;
            String query2 = queryStart + values2 + queryEnd;
            String query3 = queryStart + values3 + queryEnd;
            String query4 = queryStart + values4 + queryEnd;
            String query5 = queryStart + values5 + queryEnd;
            String query6 = queryStart + values6 + queryEnd;
            String query7 = queryStart + values7 + queryEnd;
            String query8 = queryStart + values8 + queryEnd;
            String query9 = queryStart + values9 + queryEnd;
            String query10 = queryStart + values10 + queryEnd;
            String query11 = queryStart + values11 + queryEnd;
            String query12 = queryStart + values12 + queryEnd;
            String query13 = queryStart + values13 + queryEnd;
            String query14 = queryStart + values14 + queryEnd;

            String qaInsert1 =
            Joiner.on(" ").join("insert into", DbConstans.QA_TABLE,"(",
                    DbConstans.KEY_QA_ID, ",", DbConstans.KEY_TASK_REF_ID, ",",
                    DbConstans.KEY_QUESTION, ",", DbConstans.KEY_ANSWER,
                    ") values (1, 1, 'ile masz lat?','6');");


            db.execSQL(qaInsert1);

            db.execSQL(query1);
            db.execSQL(query2);
            db.execSQL(query3);
            db.execSQL(query4);
            db.execSQL(query5);
            db.execSQL(query6);
            db.execSQL(query7);
            db.execSQL(query8);
            db.execSQL(query9);
            db.execSQL(query10);
            db.execSQL(query11);
            db.execSQL(query12);
            db.execSQL(query13);
            db.execSQL(query14);
            db.execSQL(gquery1);
            db.execSQL(gquery2);
            db.execSQL(gquery3);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVer, int newVer) {
//            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.LOCATIONS_TABLE);
            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.MARKER_TABLE);
            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.TASKS_TABLE);
            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.ALERTS_TABLE);
            _db.execSQL("DROP TABLE IF EXIST " + DbConstans.TASK_GROUPS_TABLE);

            onCreate(_db);

            Log.w("DatabaaseAdapter", "Aktualizacja bazy z wersji: " + oldVer
                    + " do: " + newVer + ". Wszystkie dane utracone.");
        }
    }
}