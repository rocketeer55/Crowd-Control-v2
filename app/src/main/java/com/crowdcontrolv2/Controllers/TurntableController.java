package com.crowdcontrolv2.Controllers;

import android.graphics.PointF;

public class TurntableController {

    public enum Direction {
        LEFT, CENTER, RIGHT;
    }

    /** Boolean representing if the turntable is being touched currently */
    private boolean isBeingTouched;

    /** Float representing the angle the turntable is currently at (relative to starting position) */
    private float currentAngle;

    /** Float representing the angular velocity of the turntable in degrees per second */
    private float angularVelocity;

    // Coordinates of center of circle
    private float xPos;
    private float yPos;

    private float startAngle;

    private float lastAngle = 0.f;

    private long lastTime = System.nanoTime();

    private int pointerId = -1;

    public TurntableController(float xPos, float yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public float getxPos() {
        return this.xPos;
    }

    public float getyPos() {
        return this.yPos;
    }

    public void setBeingTouched(boolean isBeingTouched) {
        this.isBeingTouched = isBeingTouched;
    }

    public boolean isBeingTouched() {
        return this.isBeingTouched;
    }

    public float getCurrentAngle() {
        return this.currentAngle;
    }

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }

    public int getPointerId() {
        return this.pointerId;
    }

    public void setLastAngle() {
        lastAngle = currentAngle;
    }

    public void setStartVector (PointF startVector) {
        this.startAngle = (float) Math.atan2(startVector.y, startVector.x);
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public Direction getDirection() {
        // Check if the controller isBeingTouched first!

        if (angularVelocity > 50f) {
            return Direction.LEFT;
        }
        else if (angularVelocity < -50f) {
            return Direction.RIGHT;
        }
        else {
            return Direction.CENTER;
        }
    }

    public void updateAngle(PointF currentVector) {
        float currAngle = (float) Math.atan2(currentVector.y, currentVector.x);
        float angle = currAngle - startAngle;
        float nextAngle = addLastAngle((angle * 180.f / (float) Math.PI));
        calculateAngularVelocity(nextAngle);
        currentAngle = nextAngle;
    }

    private float addLastAngle(float angle) {
        return ((angle + lastAngle) % 360.f);
    }

    private void calculateAngularVelocity(float nextAngle) {
        long currentTime = System.nanoTime();
        long deltaTime = currentTime - lastTime;
        double deltaTimeInSeconds = (double) deltaTime / 1_000_000_000;
        lastTime = currentTime;

        float deltaAngle = nextAngle - currentAngle;

        if (deltaAngle < -180.f) {
            deltaAngle += 360.f;
        } else if (deltaAngle > 180.f) {
            deltaAngle += -360.f;
        }

        angularVelocity = (float) ((double)deltaAngle / deltaTimeInSeconds);
        System.out.println(getDirection());
    }
}
