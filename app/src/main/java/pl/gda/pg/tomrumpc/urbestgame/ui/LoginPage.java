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
import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.data.SyncService;
import pl.gda.pg.tomrumpc.urbestgame.data.WsAdapter;

public class LoginPage extends Activity {

    EditText login, password;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "pl.gda.pg.tomrumpc.urbestgame.provider";
    //    public static final String AUTHORITY = "android.content.SyncAdapter";
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
        startService(new Intent(this, SyncService.class));
    }

    public void onLogin(View v) {

        String user = login.getText().toString();
        String pass = password.getText().toString();

//        // Create the account type and default account
//        Account newAccount = new Account(user, ACCOUNT_TYPE);
//        // Get an instance of the Android account manager
//        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
//
//        if (accountManager.addAccountExplicitly(newAccount, pass, Bundle.EMPTY)) {
//            startActivity(new Intent(this, Map.class));
//        } else {
//            startActivity(new Intent(this, Map.class));
//        }

        WsAdapter adapter = new WsAdapter(this);
        adapter.sendGetRequest();

        startActivity(new Intent(this, Map.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
