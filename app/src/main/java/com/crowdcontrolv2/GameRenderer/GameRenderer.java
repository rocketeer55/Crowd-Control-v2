package com.crowdcontrolv2.GameRenderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.crowdcontrolv2.Controllers.TurntableController;
import com.crowdcontrolv2.R;
import com.crowdcontrolv2.Common.RawResourceReader;
import com.crowdcontrolv2.Common.ShaderHelper;
import com.crowdcontrolv2.Common.TextureHelper;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

public class GameRenderer implements GLSurfaceView.Renderer {
    private Context mActivityContext;

    private LaneDivider[] dividers = new LaneDivider[8];
    private LinkedList<Note> notes = new LinkedList<>();

    private TurntableRenderer leftTurntable;
    private TurntableRenderer rightTurntable;

    private TurntableSpinIndicator leftTurntableSpinIndicator;
    private TurntableSpinIndicator rightTurntableSpinIndicator;

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

    private int mTextureUniformHandle;

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model color information. */
    private int mColorHandle;

    /** This will be used to pass in texture coordinate information. */
    private int mTextureCoordinateHandle;

    /** This is a handle for the simple program (used for turntable spin indicator) */
    private int mSimpleProgramHandle;

    /** This is a handle for the simple program (used for dividers and notes) */
    private int mSimpleLimitProgramHandle;

    /** This is a handle for the turntable program */
    private int mTurntableProgramHandle;

    /** How many bytes per float. */
    private final int mBytestPerFloat = 4;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;

    /** Size of the texture data in elements. */
    private final int mTextureDataSize = 2;

    private int mTextureDataHandle;

    private float ratio;

    private long lastTime = System.nanoTime();

    private float songPos;

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

        leftTurntableSpinIndicator = new TurntableSpinIndicator(-3.6f, -2.3f, -5f, 0.6f, 0.15f, 0.05f);
        rightTurntableSpinIndicator = new TurntableSpinIndicator(3.6f, -2.3f, -5f, 0.6f, 0.15f, 0.05f);
    }

    public void setLeftTurntable(TurntableController turntable) {
        this.leftTurntable = new TurntableRenderer(turntable);
        this.leftTurntableSpinIndicator.setController(turntable);
    }

    public void setRightTurntable(TurntableController turntable) {
        this.rightTurntable = new TurntableRenderer(turntable);
        this.rightTurntableSpinIndicator.setController(turntable);
    }

    protected String getSimpleVertexShaderLimit()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.simple_vertex_shader_limit);
    }

    protected String getSimpleVertexShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.simple_vertex_shader);
    }

    protected String getSimpleFragmentShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.simple_fragment_shader);
    }

    protected String getTurntableVertexShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.turntable_vertex_shader);
    }

    protected String getTurntableFragmentShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.turntable_fragment_shader);
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

        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Set the view matrix. This matrix can be said to represent the camera position.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        // Set up simple program with vertical limit
        initSimpleProgramLimit();

        // Set up simple program
        initSimpleProgram();

        // Set up turntable program
        initTurntableProgram();

    }

    private void initSimpleProgramLimit() {
        final String vertexShader = getSimpleVertexShaderLimit();
        final String fragmentShader = getSimpleFragmentShader();

        // Load in the vertex shader.
        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        // Create a program object and store the handle to it
        mSimpleLimitProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color"});
    }

    private void initSimpleProgram() {
        final String vertexShader = getSimpleVertexShader();
        final String fragmentShader = getSimpleFragmentShader();

        // Load in the vertex shader.
        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        // Create a program object and store the handle to it
        mSimpleProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color"});
    }

    private void initTurntableProgram() {
        final String vertexShader = getTurntableVertexShader();
        final String fragmentShader = getTurntableFragmentShader();

        // Load in the vertex shader.
        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        // Create a program object and store the handle to it
        mTurntableProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
               new String[] {"a_Position",  "a_TexCoordinate"});

        mTextureDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.vinyl);
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

        leftTurntable.setRatio(ratio);
        rightTurntable.setRatio(ratio);

        leftTurntableSpinIndicator.setRatio(ratio);
        rightTurntableSpinIndicator.setRatio(ratio);

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // TODO :: Remove this later
        randomlyGenerate();

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

        leftTurntable.update();
        rightTurntable.update();

        drawTurntable(leftTurntable);
        drawTurntable(rightTurntable);

        drawTurntableSpinIndicator(leftTurntableSpinIndicator);
        drawTurntableSpinIndicator(rightTurntableSpinIndicator);
    }

    private void drawDivider(final LaneDivider divider) {
        GLES20.glUseProgram(mSimpleLimitProgramHandle);

        FloatBuffer positions = divider.getPositionFloatBuffer();
        FloatBuffer colors = divider.getColorFloatBuffer();
        divider.setRatio(ratio);
        mModelMatrix = divider.getModelMatrix(mModelMatrix);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mSimpleProgramHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mSimpleProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mSimpleProgramHandle, "a_Color");

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

    private void drawTurntableSpinIndicator(final TurntableSpinIndicator indicator) {
        GLES20.glUseProgram(mSimpleProgramHandle);

        FloatBuffer positions = indicator.getPositionFloatBuffer();
        FloatBuffer colors = indicator.getColorFloatBuffer();
        indicator.setRatio(ratio);
        mModelMatrix = indicator.getModelMatrix(mModelMatrix);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mSimpleProgramHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mSimpleProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mSimpleProgramHandle, "a_Color");

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
        GLES20.glUseProgram(mSimpleLimitProgramHandle);

        FloatBuffer positions = note.getPositionFloatBuffer();
        FloatBuffer colors = note.getColorFloatBuffer();
        note.setRatio(ratio);
        mModelMatrix = note.getModelMatrix(mModelMatrix);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mSimpleProgramHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mSimpleProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mSimpleProgramHandle, "a_Color");

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

    private void drawTurntable(TurntableRenderer turntable) {
        GLES20.glUseProgram(mTurntableProgramHandle);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        FloatBuffer positions = turntable.getPositionFloatBuffer();
        FloatBuffer texcoords = turntable.getTextureFloatBuffer();

        mModelMatrix = turntable.getModelMatrix(mModelMatrix);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mTurntableProgramHandle, "u_MVPMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mTurntableProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mTurntableProgramHandle, "a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mTurntableProgramHandle, "a_TexCoordinate");

        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, positions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureDataSize, GLES20.GL_FLOAT, false,
                0, texcoords);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        GLES20.glUniform1i(mTextureUniformHandle, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    private void randomlyGenerate() {
        Random r = new Random();
        int i;
        if ((i = r.nextInt(200)) < 6) {
            switch(i) {
                case 0:
                    notes.add(new LeftYellowNote(35, r.nextInt(2) + 1));
                    break;
                case 1:
                    notes.add(new LeftBlueNote(35, r.nextInt(2) + 1));
                    break;
                case 2:
                    notes.add(new LeftRedNote(35, r.nextInt(2) + 1));
                    break;
                case 3:
                    notes.add(new RightRedNote(35, r.nextInt(2) + 1));
                    break;
                case 4:
                    notes.add(new RightBlueNote(35, r.nextInt(2) + 1));
                    break;
                case 5:
                    notes.add(new RightYellowNote(35, r.nextInt(2) + 1));
                    break;
            }
        }
    }

    public void addNote(com.crowdcontrolv2.GameRenderer.Note n)
    {
        notes.add(n);
    }

    public void setSongPos(float pos)
    {
        this.songPos = pos;
    }
}
