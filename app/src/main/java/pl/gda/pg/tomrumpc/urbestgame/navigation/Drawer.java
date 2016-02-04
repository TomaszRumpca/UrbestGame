package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Drawer extends View implements onPositionChangedListener {


    public Drawer(Context context) {
        super(context);
    }

    Paint p = new Paint();

    public Drawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        p.setARGB(255, 255, 0, 0);
    }

    Map<String, Map<String, Float>> screenCoordinates;

    private Double x, y;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int sx = canvas.getWidth();
        int sy = canvas.getHeight();

        if(screenCoordinates != null){
            for (Map.Entry entry : screenCoordinates.entrySet()) {
                Map<String, Float> coordinates = (Map<String, Float>) entry.getValue();
                Float x = coordinates.get("x");
                Float y = coordinates.get("y");
                Float z = coordinates.get("z");
                if (x != null && y != null && z!= null && z > 0) {
                    float xOnScreen = (-x * sx + sx / 2);
                    float yOnScreen =(y * sy + sy / 2);
                    canvas.drawCircle( xOnScreen, yOnScreen, 30f, p);
                    canvas.drawText((String) entry.getKey(),xOnScreen+30f,yOnScreen+30f,p);
                }
            }
        }
    }

    @Override
    public void onPositionChanged(Bundle extras) {

        List<String> tasks = extras.getStringArrayList("tasksBearingB");
        screenCoordinates = new HashMap<>();

        for (String taskName : tasks) {
            double[] bearingB = extras.getDoubleArray(taskName + "bearingB");
            Map<String, Float> coordinates = new HashMap<>();
            coordinates.put("x", (float) (bearingB[1] / bearingB[2]));
            coordinates.put("y", (float) (bearingB[0] / bearingB[2]));
            coordinates.put("z", (float) ( bearingB[2]));
            screenCoordinates.put(taskName, coordinates);
        }
        invalidate();
    }
}
