package com.crowdcontrolv2.lesson4;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.crowdcontrolv2.lesson1.LessonOneRenderer;

public class LessonFourActivity extends Activity {
    /** Hold a reference to our GLSurfaceView */
    private GLSurfaceView mGLSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

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
            mGLSurfaceView.setRenderer(new LessonFourRenderer(this));
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
