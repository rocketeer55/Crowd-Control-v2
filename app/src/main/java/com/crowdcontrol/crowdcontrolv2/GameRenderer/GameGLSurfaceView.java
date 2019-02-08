package com.crowdcontrol.crowdcontrolv2.GameRenderer;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.crowdcontrol.crowdcontrolv2.Controllers.TurntableController;

public class GameGLSurfaceView extends GLSurfaceView {
    private GameRenderer mRenderer;

    private TurntableController leftTurntableController;
    private TurntableController rightTurntableController;

    private int screenWidth;
    private int screenHeight;

    /** Any touch below this registers as a valid touch and is sent to the controllers */
    private float verticalThreshold = 0f;

    public GameGLSurfaceView(Context context) {
        super(context);

        leftTurntableController = new TurntableController(-0.5f, -0.5f);
        rightTurntableController = new TurntableController(0.5f, -0.5f);
    }

    public void setSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("Touch");

        for (int i = 0; i < event.getPointerCount(); i++) {
            // Loop through all current pointers (multi-touch)

            // Get the normalized (between -1 and 1) coordinate of each finger (i)
            float touchX = (event.getX(i)/screenWidth) * 2f - 1f;
            float touchY = (event.getY(i)/screenHeight) * 2f - 1f;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // This is the start of a touch.

                    // Check if its below the vertical threshold

                    // Check if its on left half or right half of screen, get the controller

                    // Make sure controller isn't already being touched

                    // Set startTouchX and startTouchY for that controller

                    // Set that the controller is being touched

                    // Assign the finger's pointerID to the controller
                    break;

                case MotionEvent.ACTION_MOVE:
                    // This is the continuation of a touch.

                    // Check which turntable the pointerID belongs to

                    // Set the new angle of the turntable
                    break;
                case MotionEvent.ACTION_UP:
                    // The finger has let go

                    // Check which turntable the pointerID belongs to

                    // Unset the StartTouchX and StartTouchY

                    // Set that the controller is not being touched

                    // Unset the controller's pointerID
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    public void setRenderer(GameRenderer renderer) {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }
}
