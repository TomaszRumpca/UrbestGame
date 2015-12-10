package pl.gda.pg.tomrumpc.urbestgame.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class Drawer extends View {

    private int i = 0;
    private int j = 0;

    public Drawer(Context context) {
        super(context);
    }

    public Drawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Float x, y;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (x != null && y != null) {

            Paint p = new Paint();
            p.setARGB(255, 255, 0, 0);

            int sx = canvas.getHeight();
            int sy = canvas.getWidth();

            canvas.drawCircle(-x * sx + sx / 2, y * sy + sy / 2, 30, p);
        }
    }
}
