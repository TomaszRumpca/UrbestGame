package pl.gda.pg.tomrumpc.urbestgame.data;

import com.google.common.base.Joiner;

/**
 * Created by hp on 2015-12-09.
 */
public class DbConstans {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "urbest.db";

    public static final String TASKS_TABLE = "Tasks";

    public static final String KEY_TASK_ID = "_id";
    public static final String TASK_ID_OPTION = "INTEGER PRIMARY KEY";

    public static final String KEY_TASK = "Task";
    public static final String TASK_OPTION = "TEXT NOT NULL";

    public static final String KEY_TASK_DESCRIPTION = "desc";
    public static final String TASK_DESCRIPTION_OPTION = "TEXT NOT NULL";

    public static final String KEY_MAX_POINTS = "maxPoints";
    public static final String MAX_POINTS_OPTION = "INTEGER NOT NULL";

    public static final String KEY_ACHIEVED_POINTS = "achievedPoints";
    public static final String ACHIEVED_POINTS_OPTION = "INTEGER";

    public static final String KEY_USED_PROMPTS = "usedPrompts";
    public static final String USED_PROMPTS_OPTION = "INTEGER";

    public static final String KEY_TASK_GROUP = "groupId";
    public static final String TASK_GROUP_OPTION = "INTEGER NOT NULL";

    public static final String KEY_TASK_TYPE = "type";
    public static final String TASK_TYPE_OPTION = "INTEGER NOT NULL";

    public static final String KEY_LATITUDE = "Lat";
    public static final String LATITUDE_OPTION = "TEXT NOT NULL";

    public static final String KEY_LONGITUDE = "Lon";
    public static final String LONGITUDE_OPTION = "TEXT NOT NULL";

    public static final String KEY_STATE = "State";
    public static final String STATE_OPTION = "INTEGER NOT NULL";

    public static final String KEY_DATE_OF_ACTIVATION = "ActivationDate";
    public static final String DATE_OF_ACTIVATION_OPTION = "TEXT";

    public static final String KEY_DATE_OF_COMPLETION = "CompletionDate";
    public static final String DATE_OF_COMPLETION_OPTION = "TEXT";

    //@formatter:off
    public static final String CREATE_TASKS_TABLE =
            Joiner.on(" ").join("create table", TASKS_TABLE, "(",
                    KEY_TASK_ID, TASK_ID_OPTION, ",",
                    KEY_TASK, TASK_OPTION, ",",
                    KEY_TASK_DESCRIPTION, TASK_DESCRIPTION_OPTION, ",",
                    KEY_MAX_POINTS, MAX_POINTS_OPTION, ",",
                    KEY_ACHIEVED_POINTS, ACHIEVED_POINTS_OPTION, ",",
                    KEY_USED_PROMPTS, USED_PROMPTS_OPTION, ",",
                    KEY_TASK_GROUP, TASK_GROUP_OPTION, ",",
                    KEY_TASK_TYPE, TASK_TYPE_OPTION, ",",
                    KEY_LATITUDE, LATITUDE_OPTION, ",",
                    KEY_LONGITUDE, LONGITUDE_OPTION, ",",
                    KEY_STATE, STATE_OPTION, ",",
                    KEY_DATE_OF_ACTIVATION, DATE_OF_ACTIVATION_OPTION, ",",
                    KEY_DATE_OF_COMPLETION, DATE_OF_COMPLETION_OPTION, ");");
    //@formatter:on

    ///////////////////////////////////////////////////////////////

    public static final String QA_TABLE = "qa";

    public static final String KEY_QA_ID = "id";
    public static final String QA_ID_OPTION = "INTEGER PRIMARY KEY";

    public static final String KEY_TASK_REF_ID = "taskId";
    public static final String TASK_REF_ID_OPTION = "INTEGER NOT NULL";

    public static final String KEY_QUESTION = "question";
    public static final String QUESTION_OPTION = "TEXT NOT NULL";

    public static final String KEY_ANSWER = "answer";
    public static final String ANSWER_OPTION = "TEXT NOT NULL";

    public static final String KEY_SUBMISSION_STATUS = "status";
    public static final String SUBMISSION_STATUS_OPTION = "INTEGER DEFAULT 0";
    public static final int SUBMISSION_STATUS_NOT_SUBMITTED = 0;
    public static final int SUBMISSION_STATUS_SUBMITTED = 1;

    //@formatter:off
    public static final String CREATE_QA_TABLE =
            Joiner.on(" ").join("create table", QA_TABLE, "(",
                    KEY_QA_ID, QA_ID_OPTION, ",",
                    KEY_TASK_REF_ID, TASK_REF_ID_OPTION, ",",
                    KEY_QUESTION, QUESTION_OPTION, ",",
                    KEY_ANSWER, ANSWER_OPTION, ",",
                    KEY_SUBMISSION_STATUS, SUBMISSION_STATUS_OPTION, ");");
    //@formatter:on

    ///////////////////////////////////////////////////////////////
    public static final String MARKER_TABLE = "Markers";

    public static final String KEY_MARKER_ID = "_id";
    public static final String MARKER_ID_OPTION = "INTEGER NOT NULL";

    public static final String KEY_MARKER_LATITUDE = "latitude";
    public static final String MARKER_LATITUDE_OPTION = "REAL NOT NULL";

    public static final String KEY_MARKER_LONGITUDE = "longitude";
    public static final String MARKER_LONGITUDE_OPTION = "REAL NOT NULL";

    public static final String KEY_MARKER_DRAGGABLE = "draggable";
    public static final String MARKER_DRAGGABLE_OPTION = "INTEGER NOT NULL";

    public static String KEY_MARKER_TASK = "task";
    public static final String MARKER_TASK_OPTION = "INTEGER";

    //@formatter:off
    public static final String CREATE_MARKERS_TABLE =
            Joiner.on(" ").join("create table", MARKER_TABLE, "(",
                    KEY_MARKER_ID, MARKER_ID_OPTION, ",",
                    KEY_MARKER_LATITUDE, MARKER_LATITUDE_OPTION, ",",
                    KEY_MARKER_LONGITUDE, MARKER_LONGITUDE_OPTION, ",",
                    KEY_MARKER_TASK, MARKER_TASK_OPTION, ",",
                    KEY_MARKER_DRAGGABLE, MARKER_DRAGGABLE_OPTION, ");");
    //@formatter:on


    public static final String TASK_GROUPS_TABLE = "task_groups";

    public static final String KEY_GROUP_ID = "id";
    public static final String GROUP_ID_OPTION = "INTEGER AUTO INCREMENT";

    public static final String KEY_GROUP_NAME = "name";
    public static final String GROUP_NAME_OPTION = "TEXT NOT NULL";

    public static final String KEY_GROUP_COLOR = "color";
    public static final String GROUP_COLOR_OPTION = "REAL NOT NULL";

    //@formatter:off
    public static final String CREATE_TASK_GROUPS_TABLE =
            Joiner.on(" ").join("create table", TASK_GROUPS_TABLE, "(",
                    KEY_GROUP_ID, GROUP_ID_OPTION, ",",
                    KEY_GROUP_NAME, GROUP_NAME_OPTION, ",",
                    KEY_GROUP_COLOR, GROUP_COLOR_OPTION, ");");
    //@formatter:on

    //////////////////////////////////////////////////////

    static public final int INVESTMENT_AREA = 1;
    static public final int SCULPTURES = 2;
    static public final int RIDDLE = 3;
    static public final int FOUNTAIN = 4;
    static public final int PLAYING_FIELD = 5;
    static public final int ERGO = 6;
    static public final int LAKE = 7;
    static public final int BLOCK = 8;
    static public final int BAKERY = 9;
    static public final int CROSSWORD = 10;
    static public final int OLDAIRPORT = 11;
    static public final int POPE = 12;
    static public final int FALOWIEC = 13;
    static public final int BALTIC = 14;

    ////////////////////////////////////////////////////////
}
