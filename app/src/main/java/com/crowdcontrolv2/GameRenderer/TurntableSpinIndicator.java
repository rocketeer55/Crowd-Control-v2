package com.crowdcontrolv2.GameRenderer;

import android.opengl.Matrix;

import com.crowdcontrolv2.Controllers.TurntableController;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TurntableSpinIndicator {
    public final static float[] cubePositionData = {
            // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
            // if the points are counter-clockwise we are looking at the "front". If not we are looking at
            // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
            // usually represent the backside of an object and aren't visible anyways.

            // Front face
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Back face
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // Left face
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // Top face
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            // Bottom face
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f
    };

    public final static float[] cubeColorData = {
            // Front face (grey)
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,

            // Right face (grey)
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,

            // Back face (grey)
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,

            // Left face (grey)
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,


            // Top face (grey)
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,

            // Bottom face (grey)
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f,
            0.82745098f, 0.82745098f, 0.82745098f, 0.82745098f
    };

    private TurntableController controller;

    private FloatBuffer cubePositions;
    private FloatBuffer cubeColors;

    private final int bytesPerFloat = 4;

    private float xPos;
    private float yPos;
    private float zPos;

    private float width;
    private float height;
    private float depth;

    private float ratio;

    private static float indicatorMaxMove = 1.5f;
    private static float maxAngularVelocity = 500f;

    private float indicatorOffset = 0f;

    public TurntableSpinIndicator(float xPos, float yPos, float zPos, float width, float height, float depth) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.width = width;
        this.height = height;
        this.depth = depth;

        initBuffers();
    }

    private void initBuffers() {
        // Initialize the buffers.
        cubePositions = ByteBuffer.allocateDirect(cubePositionData.length * bytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubePositions.put(cubePositionData).position(0);

        cubeColors = ByteBuffer.allocateDirect(cubeColorData.length * bytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeColors.put(cubeColorData).position(0);
    }

    public FloatBuffer getPositionFloatBuffer() {
        return cubePositions;
    }

    public FloatBuffer getColorFloatBuffer() {
        return cubeColors;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public void setController(TurntableController controller) {
        this.controller = controller;
    }

    public float[] getModelMatrix(float[] modelMatrix) {
        calculateIndicatorOffset();

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, ratio, 1, 1);
        Matrix.translateM(modelMatrix, 0, xPos, yPos, zPos);
        Matrix.translateM(modelMatrix, 0, indicatorOffset, 0, 0);
        Matrix.scaleM(modelMatrix, 0, width, height, depth);

        return modelMatrix;
    }

    private void calculateIndicatorOffset() {
        if (controller.isBeingTouched()) {
            float velocityFraction = -1 * controller.getAngularVelocity() / maxAngularVelocity;
            if (velocityFraction < 0) {
                velocityFraction = Math.max(velocityFraction, -1f);
            }
            else if (velocityFraction > 0) {
                velocityFraction = Math.min(velocityFraction, 1f);
            }
            indicatorOffset = velocityFraction * indicatorMaxMove;
        }
        else {
            indicatorOffset = 0f;
        }
    }
}
