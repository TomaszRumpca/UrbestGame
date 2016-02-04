package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import pl.gda.pg.tomrumpc.urbestgame.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigatorService extends Service implements SensorEventListener {

    public static final String NAVIGATION_SERVICE = "navigationService";
    private static final float ALPHA = 20.15f;

    SensorManager sManager;
    float[] mag, acc;
    float[] rotFromBToM = new float[9];

    ArrayList<String> taskNames;
    Map<String, double[]> tasksECEF;

    double[] userPosition;

    @Override
    public void onCreate() {
        super.onCreate();

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sAccelerometr = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor sMagneticField = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sManager.registerListener(this, sMagneticField, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, sAccelerometr, SensorManager.SENSOR_DELAY_UI);

        userPosition = new double[]{NavigationUtils.degreesToRad(54.497378d),
                NavigationUtils.degreesToRad(18.502430d), 0.5d};
    }


    @Override
    public void onDestroy() {
        sManager.unregisterListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        tasksECEF = new HashMap<>();

        Bundle extras = intent.getExtras();
        taskNames = extras.getStringArrayList("tasks");
        for (String taskName : taskNames) {
            Task task = ((Task) extras.get(taskName));
            double[] coordinates = new double[]{NavigationUtils.degreesToRad(task.getLatitude()),
                    NavigationUtils.degreesToRad(task.getLongitude()), 0.5d};
            tasksECEF.put(task.getTaskName(), coordinates);
        }

        return null;
    }


    private void sendLocationBroadcast(Bundle bundle) {
        Intent intent = new Intent(NAVIGATION_SERVICE);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] valuesArray = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
//                mag = lowPass(valuesArray.clone(), mag);
                mag = valuesArray.clone();
                  break;
            case Sensor.TYPE_GRAVITY:
                break;
            case Sensor.TYPE_ACCELEROMETER:
//                acc = lowPass(valuesArray.clone(), acc);
                acc = valuesArray.clone();
            default:
                break;
        }

        if (acc != null && mag != null) {
            boolean success = SensorManager.getRotationMatrix(rotFromBToM, null, acc, mag);
            if (success) {

                Bundle extras = new Bundle();
                if(tasksECEF != null){
                    for (Map.Entry entry : tasksECEF.entrySet()) {
                        double[] taskCoordinates = (double[]) entry.getValue();
                        double[] enu = NavigationUtils
                                .getENU(userPosition[0],userPosition[1], userPosition[2],
                                        taskCoordinates[0],taskCoordinates[1],taskCoordinates[2]);
                        float[] przemEnu = PrzemNaviUtils.latlonToENU((float) userPosition[0],
                                (float) userPosition[1], (float) userPosition[2],
                                (float) taskCoordinates[0], (float) taskCoordinates[1],
                                (float) taskCoordinates[2]);
                                float[] przemB = PrzemNaviUtils.getNameiarB(przemEnu, rotFromBToM);

                        float przemX = -przemB[1] / przemB[2];
                        float przemY = przemB[0] / przemB[2];


                        double[] bearingB = NavigationUtils.fromMToB(enu, rotFromBToM);
                        float tomX =(float) (bearingB[1] / bearingB[2]);
                        float tomY =(float) (bearingB[0] / bearingB[2]);



                        extras.putDoubleArray(entry.getKey() + "bearingB", bearingB);
                    }
                    extras.putStringArrayList("tasksBearingB", taskNames);
                    sendLocationBroadcast(extras);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected float[] lowPass(float[] newValue, float[] previousValue) {
        if (previousValue == null || Float.isNaN(previousValue[0])) {
            return newValue;
        }
        float[] calculatedValue = new float[previousValue.length];
        for (int i = 0; i < newValue.length; i++) {
            calculatedValue[i] = previousValue[i] + ALPHA * (newValue[i] - previousValue[i]);
        }
        return calculatedValue;
    }
}
