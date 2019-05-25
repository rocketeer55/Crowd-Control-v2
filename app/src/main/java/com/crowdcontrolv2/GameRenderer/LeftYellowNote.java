package com.crowdcontrolv2.GameRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LeftYellowNote extends Note {
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
        // Front face (yellow)
        1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,

        // Right face (yellow)
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,

        // Back face (yellow)
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,

        // Left face (yellow)
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,


        // Top face (yellow)
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,

        // Bottom face (yellow)
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f
    };

    protected float speed;

    private int barNo;

    public Note.POSITION getColor()
    {
        return POSITION.LEFT_YELLOW;
    }

    public LeftYellowNote(int barNo, float speed, float length) {
        this.speed = speed;

        this.barNo = barNo;

        this.xPos = -4.875f;
        this.yPos = 15f;
        this.zPos = -5f;

        this.width = 0.5f;
        this.height = length;
        this.depth = 0.05f;

        this.angle = 0;

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

    public void updatePosition(float songPos) {
        this.relativePosition = (songPosTarget - songPos) / (songPosTarget - songPosStart);
    }

    public int getBarNo()
    {
        return barNo;
    }

    public float getRelativePosition()
    {
        return relativePosition;
    }

    public float getLength()
    {
        return height;
    }
}
