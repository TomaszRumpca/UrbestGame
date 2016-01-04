package pl.gda.pg.tomrumpc.urbestgame.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import pl.gda.pg.tomrumpc.urbestgame.R;

public class LoginPage extends Activity {

    EditText login, password;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "pl.gda.pg.tomrumpc.urbestgame.provider";
    // An account type, in the form of a domain name
    public static String ACCOUNT_TYPE;

    // Instance fields
    Account mAccount;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login_page);
        this.context = getApplicationContext();
        ACCOUNT_TYPE = getResources().getString(R.string.urbest_account);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
    }

    public void onLogin(View v) {

        String user = login.getText().toString();
        String pass = password.getText().toString();

        // Create the account type and default account
        Account newAccount = new Account(user, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, pass, Bundle.EMPTY)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

            Log.d("URBEST", "Sending sync request...");
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.requestSync(newAccount, AUTHORITY, settingsBundle);
            startActivity(new Intent(this, Map.class));
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.d("URBEST", "Running map activity without syncing");
            startActivity(new Intent(this, Map.class));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
