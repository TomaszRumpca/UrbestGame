package pl.gda.pg.tomrumpc.urbestgame.data;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by torumpca on 2016-02-16.
 */
public class WsAdapter implements Response.Listener<JSONArray>, Response.ErrorListener {


    String url = "http://192.168.1.93:8080/UrbestWebAppProject/task/show";
    RequestQueue requestQueue;
    DbFacade db;

    public WsAdapter(Context context){
        this.requestQueue = Volley.newRequestQueue(context);

        this.db     = new DbFacade(context);
    }

    public void sendGetRequest(){

        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("URBEST", "Error when trying to sync data: " + error.getMessage());
    }

    @Override
    public void onResponse(JSONArray response) {

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObj = (JSONObject) response.get(i);
                Task task = Task.builder()
                        .taskId(jsonObj.getInt("id"))
                        .taskName(jsonObj.getString("taskName"))
                        .taskDescription(jsonObj.getString("taskDescription"))
                        .achivedPoints(0)
                        .maxPoints(jsonObj.getInt("maxPoints"))
                        .usedPrompts(0)
                        .dateOfActivation("")
                        .latitude(jsonObj.getDouble("latitude"))
                        .longitude(jsonObj.getDouble("longitude"))
                        .build()
                        ;
                tasks.add(task);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        db.updateDatabase(tasks);
    }
}
