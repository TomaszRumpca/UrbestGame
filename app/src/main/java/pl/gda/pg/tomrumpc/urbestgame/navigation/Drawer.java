package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class Drawer extends View implements onPositionChangedListener{


    public Drawer(Context context) {
        super(context);
    }

    Paint p = new Paint();

    public Drawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        p.setARGB(255, 255, 0, 0);
    }

    private Double x, y;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int sx = canvas.getHeight();
        int sy = canvas.getWidth();

        if (x != null && y != null) {
            canvas.drawCircle((float) -x * sx + sx / 2,(float)( y * sy + sy / 2), 30f, p);
        }
    }

    @Override
    public void onPositionChanged(double[] bearingB) {
        this.x = bearingB[0] / bearingB[2];
        this.y = bearingB[1] / bearingB[2];
        invalidate();
    }
}
