package com.crowdcontrolv2.Controllers;

import android.graphics.PointF;

public class TurntableController {

    /** Boolean representing if the turntable is being touched currently */
    private boolean isBeingTouched;

    /** Float representing the angle the turntable is currently at (relative to starting position) */
    private float currentAngle;

    // Coordinates of center of circle
    private float xPos;
    private float yPos;

    private float startAngle;

    private float lastAngle = 0.f;

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

    public void updateAngle(PointF currentVector) {
        float currAngle = (float) Math.atan2(currentVector.y, currentVector.x);
        float angle = currAngle - startAngle;
        currentAngle = addLastAngle((angle * 180.f / (float) Math.PI));
    }

    private float addLastAngle(float angle) {
        return ((angle + lastAngle) % 360.f);
    }

}
