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

    public TurntableController getLeftTurntableController() {
        return this.leftTurntableController;
    }

    public TurntableController getRightTurntableController() {
        return this.rightTurntableController;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();
        int id = event.getPointerId(actionIndex);

        TurntableController current;

        switch (action) {
            case MotionEvent.ACTION_DOWN : {
                // This is the start of any fingers touching the screen.

                float touchX = ((event.getX(actionIndex)/screenWidth) * 2f - 1f);
                float touchY = ((event.getY(actionIndex)/screenHeight) * 2f - 1f);

                // Check if its below the vertical threshold
                if (touchY < 0) {
                    return false;
                }
                // Check if its on left half or right half of screen, get the controller
                if (touchX < 0) {
                    current = leftTurntableController;
                }
                else {
                    current = rightTurntableController;
                }
                // Set startTouchX and startTouchY for that controller
                current.setStartTouchX(touchX);
                current.setStartTouchY(touchY);

                // Set that the controller is being touched
                current.setBeingTouched(true);

                // Assign the finger's pointerID to the controller
                current.setPointerId(id);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN : {
                // This is the start of another finger touching the screen.

                float touchX = ((event.getX(actionIndex)/screenWidth) * 2f - 1f);
                float touchY = ((event.getY(actionIndex)/screenHeight) * 2f - 1f);

                // Check if its below the vertical threshold
                if (touchY < 0) {
                    return false;
                }
                // Check if its on left half or right half of screen, get the controller
                if (touchX < 0) {
                    current = leftTurntableController;
                }
                else {
                    current = rightTurntableController;
                }
                // Check if the controller is already being touched
                if (current.isBeingTouched()) {
                    return false;
                }

                // Set startTouchX and startTouchY for that controller
                current.setStartTouchX(touchX);
                current.setStartTouchY(touchY);

                // Set that the controller is being touched
                current.setBeingTouched(true);

                // Assign the finger's pointerID to the controller
                current.setPointerId(id);
                break;
            }
            case MotionEvent.ACTION_UP :
            case MotionEvent.ACTION_POINTER_UP : {
                // Check which controller contains this id
                if (leftTurntableController.isBeingTouched() && leftTurntableController.getPointerId() == id) {
                    current = leftTurntableController;
                }
                else if (rightTurntableController.isBeingTouched() && rightTurntableController.getPointerId() == id) {
                    current = rightTurntableController;
                }
                else {
                    return false;
                }

                // Set it to not being touched
                current.setBeingTouched(false);

                // Set the pointer id to -1
                current.setPointerId(-1);

                break;
            }
            case MotionEvent.ACTION_MOVE : {
                for (int i = 0; i < pointerCount; i++) {
                    // Get action index of this pointer
                    int index = event.findPointerIndex(i);

                    // See which controller this pointer is assigned to
                    if (leftTurntableController.isBeingTouched() && leftTurntableController.getPointerId() == i) {
                        current = leftTurntableController;
                    }
                    else if (rightTurntableController.isBeingTouched() && rightTurntableController.getPointerId() == i) {
                        current = rightTurntableController;
                    }
                    else {continue;}

                    float touchX = ((event.getX(index)/screenWidth) * 2f - 1f);
                    float touchY = ((event.getY(index)/screenHeight) * 2f - 1f);

                    // TODO :: Calculate angle
                    float angle = 0.f;

                    current.setCurrentAngle(angle);
                }
            }
        }

        return true;
    }

    public void setRenderer(GameRenderer renderer) {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }
}
