package com.crowdcontrolv2.GameRenderer;

import android.opengl.Matrix;

import java.nio.FloatBuffer;

public abstract class Note {
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

    public abstract void updatePosition(int deltaTime);

    public float[] getModelMatrix(float[] modelMatrix) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, ratio, 1, 1);

        Matrix.rotateM(modelMatrix, 0, angle, 1f, 0f, 0f);
        Matrix.translateM(modelMatrix, 0, xPos, yPos, zPos);
        Matrix.scaleM(modelMatrix, 0, width, height, depth);

        return modelMatrix;
    }
}
