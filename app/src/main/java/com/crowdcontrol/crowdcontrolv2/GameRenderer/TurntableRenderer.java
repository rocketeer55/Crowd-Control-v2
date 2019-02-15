package com.crowdcontrol.crowdcontrolv2.GameRenderer;

import android.opengl.Matrix;

import com.crowdcontrol.crowdcontrolv2.Controllers.TurntableController;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TurntableRenderer {

    private FloatBuffer squarePositions;
    private FloatBuffer squareTextureCoordinates;

    private final int bytesPerFloat = 4;

    private float xPos;
    private float yPos;
    private float zPos;

    private float radius;

    private float rotation;
    private float ratio;

    private TurntableController controller;

    public final static float[] squarePositionData = {
            // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
            // if the points are counter-clockwise we are looking at the "front". If not we are looking at
            // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
            // usually represent the backside of an object and aren't visible anyways.

            // Front face
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
    };

   public final static float[] squareTextureCoordinateData = {
           // Front face
           -1.0f, -1.0f,
           -1.0f, 1.0f,
           1.0f, -1.0f,
           -1.0f, 1.0f,
           1.0f, 1.0f,
           1.0f, -1.0f
   };

    public TurntableRenderer(TurntableController controller) {
        this.controller = controller;

        this.xPos = controller.getxPos() * 7f;
        this.yPos = controller.getyPos() * 8.5f;
        this.zPos = -5f;

        this.radius = 2.5f;

        this.rotation = 0f;
        this.ratio = 0.0f;

        initBuffers();
    }

    private void initBuffers() {
        // Initialize the buffers.
        squarePositions = ByteBuffer.allocateDirect(squarePositionData.length * bytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        squarePositions.put(squarePositionData).position(0);

        squareTextureCoordinates = ByteBuffer.allocateDirect(squareTextureCoordinateData.length * bytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        squareTextureCoordinates.put(squareTextureCoordinateData).position(0);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        this.radius = this.radius * ratio;
    }

    public FloatBuffer getPositionFloatBuffer() {
        return squarePositions;
    }

    public FloatBuffer getTextureFloatBuffer() {
        return squareTextureCoordinates;
    }

    public void update() {
        this.rotation = controller.getCurrentAngle();
    }

    public float[] getModelMatrix(float[] modelMatrix) {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, xPos * ratio, yPos, zPos);
        Matrix.rotateM(modelMatrix, 0, rotation, 0f, 0f, 1f);
        Matrix.scaleM(modelMatrix, 0, radius, radius, 1f);

        return modelMatrix;
    }
}
