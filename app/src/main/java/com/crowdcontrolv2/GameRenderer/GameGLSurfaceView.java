package com.crowdcontrolv2.GameRenderer;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.crowdcontrolv2.Controllers.TurntableController;

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

        leftTurntableController = new TurntableController(-0.5f, -0.65f);
        rightTurntableController = new TurntableController(0.5f, -0.65f);
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
                float touchY = -1.f * ((event.getY(actionIndex)/screenHeight) * 2f - 1f);

                // Check if its below the vertical threshold
                if (touchY > 0) {
                    return false;
                }
                // Check if its on left half or right half of screen, get the controller
                if (touchX < 0) {
                    current = leftTurntableController;
                }
                else {
                    current = rightTurntableController;
                }
                // Set starting vector of controller
                PointF startingVector = new PointF(touchX - current.getxPos(), touchY - current.getyPos());
                startingVector.x *= (screenWidth / 2.f);
                startingVector.y *= (screenHeight / 2.f);
                PointF normalizedVector = normalizeVector(startingVector);
                current.setStartVector(normalizedVector);

                // Set that the controller is being touched
                current.setBeingTouched(true);

                // Assign the finger's pointerID to the controller
                current.setPointerId(id);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN : {
                // This is the start of another finger touching the screen.

                float touchX = ((event.getX(actionIndex)/screenWidth) * 2f - 1f);
                float touchY = -1.f * ((event.getY(actionIndex)/screenHeight) * 2f - 1f);

                // Check if its below the vertical threshold
                if (touchY > 0) {
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
                PointF startingVector = new PointF(touchX - current.getxPos(), touchY - current.getyPos());
                startingVector.x *= (screenWidth / 2.f);
                startingVector.y *= (screenHeight / 2.f);
                current.setStartVector(normalizeVector(startingVector));

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

                current.setLastAngle();
                // Set it to not being touched
                current.setBeingTouched(false);

                // Set the pointer id to -1
                current.setPointerId(-1);

                break;
            }
            case MotionEvent.ACTION_MOVE : {
                // Check movement for left controller
                int leftTurntableIndex = event.findPointerIndex(leftTurntableController.getPointerId());
                int rightTurntableIndex = event.findPointerIndex(rightTurntableController.getPointerId());

                if (leftTurntableIndex >= 0 && leftTurntableController.isBeingTouched()) {
                    current = leftTurntableController;

                    float touchX = ((event.getX(leftTurntableIndex)/screenWidth) * 2f - 1f);
                    float touchY = -1.f * ((event.getY(leftTurntableIndex)/screenHeight) * 2f - 1f);

                    PointF currentVector = new PointF(touchX - current.getxPos(), touchY - current.getyPos());
                    currentVector.x *= (screenWidth / 2.f);
                    currentVector.y *= (screenHeight / 2.f);
                    PointF normalizedVector = normalizeVector(currentVector);
                    current.updateAngle(normalizedVector);
                }

                if (rightTurntableIndex >= 0 && rightTurntableController.isBeingTouched()) {
                    current = rightTurntableController;

                    float touchX = ((event.getX(rightTurntableIndex)/screenWidth) * 2f - 1f);
                    float touchY = -1.f * ((event.getY(rightTurntableIndex)/screenHeight) * 2f - 1f);

                    PointF currentVector = new PointF(touchX - current.getxPos(), touchY - current.getyPos());
                    currentVector.x *= (screenWidth / 2.f);
                    currentVector.y *= (screenHeight / 2.f);
                    PointF normalizedVector = normalizeVector(currentVector);
                    current.updateAngle(normalizedVector);
                }
            }
        }

        return true;
    }

    public void setRenderer(GameRenderer renderer) {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }

    private PointF normalizeVector(PointF vector) {
        double length = Math.sqrt (vector.x * vector.x + vector.y * vector.y);
        float newX = 0.f;
        float newY = 0.f;
        if (length > 0) {
            newX = vector.x / (float)length;
            newY = vector.y / (float)length;
        }
        return new PointF(newX, newY);
    }
}
