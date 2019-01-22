package com.crowdcontrol.crowdcontrolv2;

import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;

public class GameRenderer implements GLSurfaceView.Renderer {
    /* Store our model data in a float buffer. */
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle2Vertices;
    private final FloatBuffer mTriangle3Vertices;

    /* How many bytes per float. */
    private final int mBytestPerFloat = 4;

    /**
     * Initialize the model data.
     */

    public GameRenderer() {
        // This triangle is red, green, and blue.
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f
        };

        
    }
}
