package com.crowdcontrol.crowdcontrolv2.Controllers;

public class TurntableController {

    /** Boolean representing if the turntable is being touched currently */
    private boolean isBeingTouched;

    /** Float representing the angle the turntable is currently at (relative to starting position) */
    private float currentAngle;

    private float xPos;
    private float yPos;

    private float startTouchX;
    private float startTouchY;

    private int pointerId = -1;

    public TurntableController(float xPos, float yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setBeingTouched(boolean isBeingTouched) {
        this.isBeingTouched = isBeingTouched;
    }

    public boolean isBeingTouched() {
        return this.isBeingTouched;
    }

    public void setCurrentAngle(float currentAngle) {
        this.currentAngle = currentAngle;
    }

    public float getCurrentAngle() {
        return this.currentAngle;
    }

    public float getxPos() {
        return this.xPos;
    }

    public float getyPos() {
        return this.yPos;
    }

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }

    public int getPointerId() {
        return this.pointerId;
    }

    public float getStartTouchX() {
        return startTouchX;
    }

    public void setStartTouchX(float startTouchX) {
        this.startTouchX = startTouchX;
    }

    public float getStartTouchY() {
        return startTouchY;
    }

    public void setStartTouchY(float startTouchY) {
        this.startTouchY = startTouchY;
    }

}
