package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;

public class NavigatorService extends Service implements SensorEventListener {

    public static final String NAVIGATION_SERVICE = "navigationService";
    private static final float ALPHA = 0.65f;

    SensorManager sManager;
    float[] mag, acc;
    float[] rotFromBToM = new float[9];

    double[] enu;

    @Override
    public void onCreate() {
        super.onCreate();

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sGravity = sManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor sAccelerometr = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor sMagneticField = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sManager.registerListener(this, sGravity, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, sMagneticField, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, sAccelerometr, SensorManager.SENSOR_DELAY_UI);


        double[] userPosition = new double[]{54.497378d, 18.502430d, 0f};
        enu = NavigationUtils.getENU(
                NavigationUtils.degreesToRad(userPosition[0]),
                NavigationUtils.degreesToRad(userPosition[1]),
                userPosition[2],
                NavigationUtils.geoCoordinatesToECEF(
                        NavigationUtils.degreesToRad(54.497388d),
                        NavigationUtils.degreesToRad(18.502430d),
                        0d));
    }

    @Override
    public void onDestroy() {
        sManager.unregisterListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendLocationBroadcast(double[] b) {
        Intent intent = new Intent(NAVIGATION_SERVICE);
        intent.putExtra("bearingB", b);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] valuesArray = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mag = lowPass(valuesArray.clone(), mag);
                break;
            case Sensor.TYPE_GRAVITY:
                break;
            case Sensor.TYPE_ACCELEROMETER:
                acc = lowPass(valuesArray.clone(), acc);
            default:
                break;
        }

        if (acc != null && mag != null) {
            boolean success = SensorManager.getRotationMatrix(rotFromBToM, null, acc, mag);
            if (success) {
                double[] bearingB = NavigationUtils.fromMToB(enu, rotFromBToM);
                sendLocationBroadcast(bearingB);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}
