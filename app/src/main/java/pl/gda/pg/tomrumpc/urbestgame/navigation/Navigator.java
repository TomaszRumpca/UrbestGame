package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import pl.gda.pg.tomrumpc.urbestgame.R;

public class Navigator extends AppCompatActivity implements SensorEventListener {

    SensorManager sManager;
    Sensor sGravity, sMagneticField, sAccelerometr;
    float[] mag, acc;

    float[] rotFromBToM = new float[9];

    float[] bearingB = new float[]{0, 1, 0};

    float[] WarszawaEnu;

    HashMap<String, float[]> objects = new HashMap<>();

    float[] userPosition = new float[]{18, 55, 0};
    Drawer mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sGravity = sManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sAccelerometr = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sMagneticField = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mView = (Drawer) findViewById(R.id.drawer);

        WarszawaEnu = NavigationUtils.getENU(userPosition[0], userPosition[1], userPosition[2],
                NavigationUtils.LatLonToECEF(52.244629f, 21.016556f, 0f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, sGravity, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, sMagneticField, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, sAccelerometr, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] valuesArray = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mag = valuesArray.clone();
                break;
            case Sensor.TYPE_GRAVITY:
                break;
            case Sensor.TYPE_ACCELEROMETER:
                acc = valuesArray.clone();
            default:
                break;
        }

        if (acc != null && mag != null) {

            boolean success = SensorManager.getRotationMatrix(rotFromBToM, null, acc, mag);

            if (success) {

                bearingB = NavigationUtils.fromMToB(WarszawaEnu, rotFromBToM);

                mView.x = bearingB[0] / bearingB[2];
                mView.y = bearingB[1] / bearingB[2];
                mView.invalidate();
            }
        }

    }


    public String getObject(float[] cameraM) {
        double min = 100000000d;
        String str = "";
        for (Map.Entry e : objects.entrySet()) {
            double cAngle = NavigationUtils.getAngle((float[]) e.getValue(), cameraM);
            if (cAngle < min) {
                min = cAngle;
                str = (String) e.getKey();
            }
        }
        return str;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
