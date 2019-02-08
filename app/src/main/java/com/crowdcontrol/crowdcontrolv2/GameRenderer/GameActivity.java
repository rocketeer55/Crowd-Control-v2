package com.crowdcontrol.crowdcontrolv2.GameRenderer;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

import com.crowdcontrol.crowdcontrolv2.lesson1.LessonOneRenderer;

public class GameActivity extends Activity {
    /** Hold a reference to our GLSurfaceView */
    private GameGLSurfaceView mGLSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GameGLSurfaceView(this);

        WindowManager windowManager = getWindowManager();
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);

        mGLSurfaceView.setSize(size.x, size.y);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        boolean supportsEs2 = false;
        try {

            final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
            supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        }
        catch (NullPointerException e) {
            supportsEs2 = false;
        }

        if (supportsEs2) {
            // Request a OpenGL ES 2.0 Context
            mGLSurfaceView.setEGLContextClientVersion(2);

            // Set the renderer to our renderer
            mGLSurfaceView.setRenderer(new GameRenderer(this));
        }

        else {
            // Do something here if the device doesn't support OpenGL ES 2.0
            return;
        }



        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        // The activity must call the GL surface view's onResume()0 on activity onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
