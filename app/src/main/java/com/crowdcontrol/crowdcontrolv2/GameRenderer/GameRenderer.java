package com.crowdcontrol.crowdcontrolv2.GameRenderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.view.SurfaceView;

import com.crowdcontrol.crowdcontrolv2.R;
import com.crowdcontrol.crowdcontrolv2.common.RawResourceReader;
import com.crowdcontrol.crowdcontrolv2.common.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

public class GameRenderer implements GLSurfaceView.Renderer {
    private Context mActivityContext;

    private LaneDivider[] dividers = new LaneDivider[8];
    private LinkedList<Note> notes = new LinkedList<>();

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each
     * model can be thought of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space.
     * It positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix = new float[16];

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];

    /** This will be used to pass in teh transformation matrix. */
    private int mMVPMatrixHandle;

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model color information. */
    private int mColorHandle;

    /** How many bytes per float. */
    private final int mBytestPerFloat = 4;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;

    private float ratio;

    private long lastTime = System.nanoTime();
    /**
     * Initialize the model data.
     */
    public GameRenderer(Context c) {
        mActivityContext = c;

        dividers[0] = new LaneDivider(-5.5f, 2f, -5f, 0.05f, 5f, 0.05f);
        dividers[1] = new LaneDivider(-4.25f, 2f, -5f, 0.05f, 5f, 0.05f);
        dividers[2] = new LaneDivider(-3f, 2f, -5f, 0.05f, 5f, 0.05f);
        dividers[3] = new LaneDivider(-1.75f, 2f, -5f, 0.05f, 5f, 0.05f);
        dividers[4] = new LaneDivider(1.75f, 2f, -5f, 0.05f, 5f, 0.05f);
        dividers[5] = new LaneDivider(3f, 2f, -5f, 0.05f, 5f, 0.05f);
        dividers[6] = new LaneDivider(4.25f, 2f, -5f, 0.05f, 5f, 0.05f);
        dividers[7] = new LaneDivider(5.5f, 2f, -5f, 0.05f, 5f, 0.05f);

        notes.add(new YellowNote(10f, 3f));
    }

    protected String getVertexShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.simple_vertex_shader_limit);
    }

    protected String getFragmentShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.simple_fragment_shader);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

        // Set the background clear color to gray.
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // We are looking towards the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader = getVertexShader();
        final String fragmentShader = getFragmentShader();

        // Load in the vertex shader.
        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        // Create a program object and store the handle to it
        int programHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color"});

        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Tell OppenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        long time = System.nanoTime();
        int deltaTime = (int) ((time - lastTime) / 1000000); // In milliseconds
        lastTime = time;

        for (LaneDivider d : dividers) {
            drawDivider(d);
        }

        for (Note n : notes) {
            n.updatePosition(deltaTime);
            drawNote(n);
        }
    }

    private void drawDivider(final LaneDivider divider) {
        FloatBuffer positions = divider.getPositionFloatBuffer();
        FloatBuffer colors = divider.getColorFloatBuffer();
        divider.setRatio(ratio);
        mModelMatrix = divider.getModelMatrix(mModelMatrix);

        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, positions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                0, colors);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    private void drawNote(final Note note) {
        FloatBuffer positions = note.getPositionFloatBuffer();
        FloatBuffer colors = note.getColorFloatBuffer();
        note.setRatio(ratio);
        mModelMatrix = note.getModelMatrix(mModelMatrix);

        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, positions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                0, colors);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }
}
