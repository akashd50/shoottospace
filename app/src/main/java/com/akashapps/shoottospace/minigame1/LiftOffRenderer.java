package com.akashapps.shoottospace.minigame1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.akashapps.shoottospace.TouchController;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LiftOffRenderer implements GLSurfaceView.Renderer {
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private Context context;
    private TouchController controller;

    public LiftOffRenderer(Context ctx, TouchController controller) {
        this.context = ctx;
        this.controller = controller;

    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    }

    public void onDrawFrame(GL10 unused) {
        android.opengl.Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f,
                0.0f, 0.0f, 0.0f,
                0f, 1.0f, 0.0f);
        android.opengl.Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        android.opengl.Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 100);

    }
}
