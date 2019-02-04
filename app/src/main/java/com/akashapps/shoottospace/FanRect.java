package com.akashapps.shoottospace;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;

public class FanRect {
    private int scrWidth,scrHeight;

    private float left,top, right, bottom;
    private float transformY,transformX;

    private boolean active;

    //private float color[];
    private Triangle t1;//= new Triangle(v1,c);
    private Triangle t2;
    private float ar1[],ar2[];
    private Context context;

    private float cx,cy,cz,l,h;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private int colorCount;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private final int COLOR_COMPONENTS_COUNT = 4;
    private static final int COORDS_PER_VERTEX = 3;

    private int vertexCount;// = triangleCoords.length / COORDS_PER_VERTEX;
    //private int vertexStride = (COORDS_PER_VERTEX + COLOR_COMPONENTS_COUNT)* 4; // 4 bytes per vertex

    private int vertexStride = (COORDS_PER_VERTEX )* 4; // 4 bytes per vertex
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 uMVPMatrix;"+
                    "attribute vec4 a_Color;"+
                    "varying vec4 v_Color;"+
                    "void main() {" +
                    "v_Color = a_Color;"+
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    //"  gl_PointSize = 10.0"+
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    //"uniform vec4 vColor;" +
                    "varying vec4 v_Color;"+
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}";

    private int mMVPMatrixHandle;

    public FanRect(float cx,float cy, float cz,float l, float h, float[] c, Context ctx) {
        //color = c;
        this.cx = cx;this.cy = cy;this.cz = cz;
        this.l = l;this.h = h;

        transformY = 0f;transformX = 0f;active = false;

        float v1[] = {cx,cy,cz,
                cx-(l/2),cy-(h/2),cz,
                cx+(l/2),cy-(h/2),cz,
                cx+(l/2),cy+(h/2),cz,
                cx-(l/2),cy+(h/2),cz,
                cx-(l/2),cy-(h/2),cz};

        float[] colorAr = {104/255f, 182/255f, 242/255f, 1.0f,
                            194/255f, 227/255f, 252/255f, 0.0f,
                            194/255f, 227/255f, 252/255f, 0.0f,
                            194/255f, 227/255f, 252/255f, 0.0f,
                            194/255f, 227/255f, 252/255f, 0.0f,
                            194/255f, 227/255f, 252/255f, 0.0f};

        //ar1 = v1;
        //color = colorAr;
        scrWidth = getScreenWidth();
        scrHeight = getScreenHeight();

        vertexCount = v1.length/COORDS_PER_VERTEX ;
        ByteBuffer bb = ByteBuffer.allocateDirect(v1.length * 4);
        if(bb!=null) {
            // (number of coordinate values * 4 bytes per float)

            // use the device hardware's native byte order
            bb.order(ByteOrder.nativeOrder());

            // create a floating point buffer from the ByteBuffer
            vertexBuffer = bb.asFloatBuffer();
            // add the coordinates to the FloatBuffer
            vertexBuffer.put(v1);
            // set the buffer to read the first coordinate
            vertexBuffer.position(0);
            colorCount = colorAr.length / COLOR_COMPONENTS_COUNT;
            ByteBuffer cb = ByteBuffer.allocateDirect(colorAr.length * 4);
            cb.order(ByteOrder.nativeOrder());
            colorBuffer = cb.asFloatBuffer();
            colorBuffer.put(colorAr);
            colorBuffer.position(0);

            int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);

            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram();

            // add the vertex shader to program
            GLES20.glAttachShader(mProgram, vertexShader);

            // add the fragment shader to program
            GLES20.glAttachShader(mProgram, fragmentShader);

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(mProgram);
        }else{
            Toast.makeText(ctx,"ByteBuff Fail",Toast.LENGTH_SHORT).show();
        }

        context = ctx;
        //convertPointSystem();
    }

    public void draw(float[] mvpMatrix) {
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

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
        //GLES20.glDrawArrays(GLES20.GL_LINES);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    public void convertPointSystem(){
        this.left = (scrWidth/2 + ar1[0]*scrWidth/2) + (this.transformX*(scrWidth/2));
        this.top = (float) (scrHeight/2-(ar1[1]*scrHeight/2)/1.7);
        this.right = (scrWidth/2 + ar2[0]*scrWidth/2) + (this.transformX*(scrWidth/2));
        // this.bottom = (int) (scrHeight/2- ar2[1]*scrHeight/2);
        this.bottom = (float) (scrHeight/2-(ar2[1]*scrHeight/2)/1.7);
    }

    public boolean isClicked(float tx, float ty){
        if(tx > left && tx < right && ty < bottom && ty > top) {
            return true;
        }else return false;
    }

    public static int getScreenWidth () {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight () {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void updateTrasnformY(float vy){
        transformY+=vy;
    }
    public float getTransformY(){return this.transformY;}

    public void updateTrasnformX(float vx){
        transformX+=vx;
        //convertPointSystem();
    }
    public void changeTrasnformX(float vx){
        transformX =vx;
        //convertPointSystem();
    }
    public void changeTrasnformY(float vx){
        transformY =vx;
        //convertPointSystem();
    }
    public float getTransformX(){return this.transformX;}

    public void deactivate(){
        active = false;
        transformY = 0;
        transformX = 0;
        //transformY = 0f;
    }
    public void activate(){active = true;}

    public boolean isActive(){return this.active;}

    public float getCX(){return this.cx;}
    public float getCY(){return this.cy;}
    public float getCZ(){return this.cz;}
}
