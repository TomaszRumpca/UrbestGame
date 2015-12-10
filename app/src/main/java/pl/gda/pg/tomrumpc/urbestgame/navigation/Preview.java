package pl.gda.pg.tomrumpc.urbestgame.navigation;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;

    Preview(Context context) {
        super(context);
        mHolder = getHolder();

        mHolder.addCallback(this);

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public Preview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();

        mHolder.addCallback(this);

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {

        if (camera == null) {

            camera = Camera.open();
            camera.setDisplayOrientation(90);
            Camera.Parameters cp = camera.getParameters();
            cp.setPreviewSize(600, 480);
            camera.setParameters(cp);

            try {
                camera.setPreviewDisplay(holder);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        camera.startPreview();
    }
}