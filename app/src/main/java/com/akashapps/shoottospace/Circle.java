package com.akashapps.shoottospace;

import android.opengl.GLES20;
import android.util.FloatMath;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;

public class Circle {
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private static final int COLOR_COMPONENTS_COUNT = 4;
    private static final int COORDS_PER_VERTEX = 3;
    private int vertexStride = (COORDS_PER_VERTEX )* 4;
    private int vertexCount;
    private static final String CVXSHADERCODE =
                    "attribute vec4 vPosition;" +
                    "uniform mat4 uMVPMatrix;"+
                    "attribute vec4 a_Color;"+
                    "varying vec4 v_Color;"+
                    "void main() {" +
                    "v_Color = a_Color;"+
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    //"  gl_PointSize = 10.0"+
                    "}";

    private static final String CFGSHADERCODE =
                    "precision mediump float;" +
                    //"uniform vec4 vColor;" +
                    "varying vec4 v_Color;"+
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}";

    private int mMVPMatrixHandle;

    private float cordsX[], cordsY[], coordinates[], color[];
    public Circle(SimpleVector center,int q, float r){
       // cordsX = new float[q*360+1];
       // cordsY = new float[q*360+1];
        color = new float[360*4+8];
        coordinates = new float[360*COORDS_PER_VERTEX + 6];
        int offset = 0;
        int offset2 = 0;
        vertexCount = coordinates.length/COORDS_PER_VERTEX;
        coordinates[offset++] = center.x;
        coordinates[offset++] = center.y;
        coordinates[offset++] = center.z;
        //coordinates[offset++] = 1f;

        color[offset2++] = 0.5f;
        color[offset2++] = 0.3f;
        color[offset2++] = 0.9f;
        color[offset2++] = 1f;

        for(int i=0;i<=360;i++){
            //cordsX[i] = (float)(r* Math.cos(i*Math.PI/180));
            //cordsY[i] = (float)(r*Math.sin(i*Math.PI/180));
            float angle = /*(float)(i*(Math.PI/360));*/((float) i / (float) 360)
                    * ((float) Math.PI * 2f);
            coordinates[offset++] = center.x + (float)(r* Math.cos(angle));
            coordinates[offset++] = center.y+ (float)(r* Math.sin(angle));
            coordinates[offset++] = center.z;
           // coordinates[offset++] = 1f;

            color[offset2++] = 1.0f;
            color[offset2++] = 0f;
            color[offset2++] = 0f;
            color[offset2++] = 1f;
        }
        vertexCount = coordinates.length/COORDS_PER_VERTEX;
        ByteBuffer bb = ByteBuffer.allocateDirect(coordinates.length*4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);

        ByteBuffer cb = ByteBuffer.allocateDirect(color.length*4);
        cb.order(ByteOrder.nativeOrder());

        colorBuffer = cb.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                CVXSHADERCODE);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                CFGSHADERCODE);
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }


    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);


        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle,COLOR_COMPONENTS_COUNT,
                GLES20.GL_FLOAT,false,
                COLOR_COMPONENTS_COUNT*4,colorBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        // Set color for drawing the triangle
        // GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // Draw the triangle
        GLES20.glEnable( GL_BLEND );
        GLES20.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
        //GLES20.glDrawArrays(GLES20.GL_LINES);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisable(mColorHandle);
    }

}
