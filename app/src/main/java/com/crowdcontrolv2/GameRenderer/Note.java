package com.crowdcontrolv2.GameRenderer;

import android.opengl.Matrix;

import java.nio.FloatBuffer;

import MusicHandler.BeatMap;

public abstract class Note {

    public enum POSITION
    {
        LEFT_BLUE, RIGHT_BLUE, LEFT_YELLOW, RIGHT_YELLOW, LEFT_RED, RIGHT_RED
    }

    protected FloatBuffer cubePositions;
    protected FloatBuffer cubeColors;

    protected final int bytesPerFloat = 4;

    protected float xPos;
    protected float yPos;
    protected float zPos;

    protected float width;
    protected float height;
    protected float depth;

    protected float angle;
    protected float ratio;

    protected float speed;

    //Brad Stuff
    public int measure;
    public BeatMap.NOTE_LENGTH noteLength;
    protected float songPosTarget;
    protected float songPosStart;
    protected float relativePosition;


    public FloatBuffer getPositionFloatBuffer() {
        return cubePositions;
    }

    public FloatBuffer getColorFloatBuffer() {
        return cubeColors;
    }

    public void setRotation(float angle) {
        this.angle = angle;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public abstract void updatePosition(float songPosition);

    public abstract Note.POSITION getColor();

    public float[] getModelMatrix(float[] modelMatrix) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, ratio, 1, 1);

        Matrix.rotateM(modelMatrix, 0, angle, 1f, 0f, 0f);
        Matrix.translateM(modelMatrix, 0, xPos, yPos, zPos);
        Matrix.scaleM(modelMatrix, 0, width, height, depth);

        return modelMatrix;
    }

    public void setTarget(float songPosStart, float songPosTarget)
    {
        this.songPosStart = songPosStart;
        this.songPosTarget = songPosTarget;
    }

    public abstract int getBarNo();

    public abstract float getRelativePosition();

    public abstract float getLength();
}
