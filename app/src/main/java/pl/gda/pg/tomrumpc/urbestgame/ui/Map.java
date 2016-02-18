package pl.gda.pg.tomrumpc.urbestgame.ui;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import pl.gda.pg.tomrumpc.urbestgame.Constans;
import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.data.DbFacade;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;

import java.util.List;

public class Map extends AbstractUrbestActivity {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.content_welcome_page);
        stub.inflate();

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.setMyLocationEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        CameraPosition cameraPosition =
                new CameraPosition.Builder().target(new LatLng(54.497378, 18.502430)).zoom(10)
                        .bearing(0).tilt(30) // Sets the tilt of the camera to 30 degrees
                        .build(); // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        DbFacade db = new DbFacade(getApplicationContext());
        List<Task> tasks = db.getTasks(Constans.DEFAULT_TASK_GROUP_NAME);

        for (Task task : tasks) {
            String title = task.getTaskName();
            MarkerOptions markerFountain =
                    new MarkerOptions().position(task.getLatLng()).draggable(false)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(title);
            map.addMarker(markerFountain);
        }

    }


}
