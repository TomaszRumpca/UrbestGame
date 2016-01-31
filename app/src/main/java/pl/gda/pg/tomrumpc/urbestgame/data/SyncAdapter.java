package pl.gda.pg.tomrumpc.urbestgame.data;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by torumpca on 2016-01-03.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
        implements Response.Listener<JSONArray>, Response.ErrorListener {

    String url = "http://192.168.1.93:8080/UrbestWebAppProject/task/show";
    RequestQueue requestQueue;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize, true);
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
            ContentProviderClient provider, SyncResult syncResult) {

        Log.d("URBEST", "Performing sync...");
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("URBEST","Error when trying to sync data: "+error.getMessage());
    }

    @Override
    public void onResponse(JSONArray response) {

        try {
            String responseType = response.getString(0);

            switch (responseType){
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < response.length(); i++){

        }


        Log.d("URBEST", "Getting the response from server");
        try {
            Log.d("URBEST",response.getString(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                // do I have to cancel this?
                return true; // -> always yes
            }
        });
        Log.d("URBEST", "Sync canceled");
    }
}
