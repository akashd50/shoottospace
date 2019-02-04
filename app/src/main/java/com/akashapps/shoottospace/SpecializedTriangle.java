package com.akashapps.shoottospace;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;

public class SpecializedTriangle {
        private int scrWidth,scrHeight;

        private FloatBuffer vertexBuffer;
        private FloatBuffer colorBuffer;
        private int colorCount;
        private int mProgram;
        private int mPositionHandle;
        private int mColorHandle;
        private final int COLOR_COMPONENTS_COUNT = 4;
        private static final int COORDS_PER_VERTEX = 4;

        private int vertexCount;// = triangleCoords.length / COORDS_PER_VERTEX;
        //private int vertexStride = (COORDS_PER_VERTEX + COLOR_COMPONENTS_COUNT)* 4; // 4 bytes per vertex

        private int vertexStride = (COORDS_PER_VERTEX )* 4; // 4 bytes per vertex
        private static final String STVXSHADERCODE =
                "attribute vec4 vPosition;" +
                        "uniform mat4 uMVPMatrix;"+
                        "attribute vec4 a_Color;"+
                        "varying vec4 v_Color;"+
                        "void main() {" +
                        "v_Color = a_Color;"+
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        //"  gl_PointSize = 10.0"+
                        "}";

        private static final String STFGSHADERCODE =
                        "precision mediump float;" +
                        //"uniform vec4 vColor;" +
                        "varying vec4 v_Color;"+
                        "void main() {" +
                        "  gl_FragColor = v_Color;" +
                        "}";

        private int mMVPMatrixHandle;

        // number of coordinates per vertex in this array
        private float triangleCoords[];/* = {   // in counterclockwise order:
            0.0f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };*/

        // Set color with red, green, blue and alpha (opacity) values
        private float color[];// = { 0.0f, 0.5f, 0.0f, 1.0f };

        public SpecializedTriangle(float[] vert, float[] c) {

            color = c;
            // initialize vertex byte buffer for shape coordinates
            triangleCoords= vert;
            vertexCount = triangleCoords.length/COORDS_PER_VERTEX ;
            ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
            // (number of coordinate values * 4 bytes per float)

            // use the device hardware's native byte order
            bb.order(ByteOrder.nativeOrder());

            // create a floating point buffer from the ByteBuffer
            vertexBuffer = bb.asFloatBuffer();
            // add the coordinates to the FloatBuffer
            vertexBuffer.put(triangleCoords);
            // set the buffer to read the first coordinate
            vertexBuffer.position(0);

            colorCount = color.length/COLOR_COMPONENTS_COUNT;
            ByteBuffer cb = ByteBuffer.allocateDirect(color.length*4);
            cb.order(ByteOrder.nativeOrder());
            colorBuffer = cb.asFloatBuffer();
            colorBuffer.put(color);
            colorBuffer.position(0);

            int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                    STVXSHADERCODE);
            int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    STFGSHADERCODE);

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
            // get handle to shape's transformation matrix
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

            // Enable a handle to the triangle vertices

            // get handle to vertex shader's vPosition member
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            // Prepare the triangle coordinate datav
            //vertexBuffer.position(0);
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    vertexStride, vertexBuffer);
            GLES20.glEnableVertexAttribArray(mPositionHandle);



            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
            //vertexBuffer.position(COORDS_PER_VERTEX); //start from 3... reading color data from the matrix
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

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
            //GLES20.glDrawArrays(GLES20.GL_LINES);

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisable(mColorHandle);
        }

}
