package pl.gda.pg.tomrumpc.urbestgame.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by torumpca on 2016-01-02.
 */
public class GameDataProvider extends ContentProvider {

    public static final String AUTHORITY = "pl.gda.pg.tomrumpc.urbestgame.provider";
    public static final String URL = "content://" + AUTHORITY + "/GameData";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static UriMatcher uriMatcher;
    private static final int GROUPS_ID = 0;
    private static final int TASKS_ID = 1;


    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "groups/#", GROUPS_ID);
        uriMatcher.addURI(AUTHORITY, "tasks/#", TASKS_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        Cursor cursor = null;

        switch(uriMatcher.match(uri)){
            case GROUPS_ID:
                break;
            case TASKS_ID:
                break;
            default:
                throw new IllegalArgumentException("No content provider uri match");
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
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
}
