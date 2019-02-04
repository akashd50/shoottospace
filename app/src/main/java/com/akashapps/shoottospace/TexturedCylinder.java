package com.akashapps.shoottospace;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;

public class TexturedCylinder {
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer, textureBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private static final int COLOR_COMPONENTS_COUNT = 4;
    private static final int COORDS_PER_VERTEX = 3;
    private int vertexStride = (COORDS_PER_VERTEX )* 4;
    private int vertexCount;
    private int[] textures;
    private static final String CVXSHADERCODE =
                    "attribute vec4 vPosition;" +
                    "uniform mat4 uMVPMatrix;"+
                   // "attribute vec4 a_Color;"+
                   // "varying vec4 v_Color;"+
                    "attribute vec2 a_TextureCoordinates;" +
                   // "attribute vec3 a_light;"+
                   // "varying vec3 v_Color;"+
                    "varying vec2 v_TextureCoordinates;" +
                    "void main() {" +
                  //  "v_Color = a_Color;"+
                        "v_TextureCoordinates = a_TextureCoordinates;" +
                        "gl_Position = uMVPMatrix * vPosition;" +
                    //"  gl_PointSize = 10.0"+
                    "}";

    private static final String CFGSHADERCODE =
                    "precision mediump float;" +
                    "uniform sampler2D u_TextureUnit;" +
                    //"uniform vec4 vColor;" +
                 //   "varying vec4 v_Color;"+
                    "varying vec2 v_TextureCoordinates;" +
                    "void main() {" +
                        "gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);" +
                    "}";

    private int mMVPMatrixHandle, textureUniform;

    private float cordsX[], cordsY[], coordinates[], color[];
    public float translateX, translateY, translateZ, rotateX, rotateY, rotateZ, scaleX,
            scaleY, scaleZ,defTransX, defTransY, defTransZ;

    public TexturedCylinder(SimpleVector center, int q, float r, float h, int resID){
        translateX = 0f; translateY=0f; translateZ=0f; rotateX=0f; rotateY=0f; rotateZ=0f;
        scaleX=1f; scaleY=1f; scaleZ = 1f;

        int numPoints = 360/q;
        coordinates = new float[(2*numPoints)*COORDS_PER_VERTEX + 6];
        float[] textureCoords = new float[(2*numPoints)*2+4];

        int offset = 0;
        //int offset2 = 0;
        int texOffset = 0;
        vertexCount = coordinates.length/COORDS_PER_VERTEX;
        for(int i=0;i<=360;i+=q){
            float angle = ((float) i / (float) 360)
                    * ((float) Math.PI * 2f);
            float base = center.x + (float)(r* Math.cos(angle));
            float height = center.y+ (float)(r* Math.sin(angle));
            coordinates[offset++] = base;
            coordinates[offset++] = height;
            coordinates[offset++] = 0-h/2;

            textureCoords[texOffset++] = (float)((1.0/360)*i);
            textureCoords[texOffset++] = 1.0f;

            coordinates[offset++] = base;
            coordinates[offset++] = height;
            coordinates[offset++] = 0 + h/2;

            textureCoords[texOffset++] = (float)((1.0/360)*i);
            textureCoords[texOffset++] = 0f;

        }
        vertexCount = coordinates.length/COORDS_PER_VERTEX;
        ByteBuffer bb = ByteBuffer.allocateDirect(coordinates.length*4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);

        ByteBuffer tb = ByteBuffer.allocateDirect(textureCoords.length*4);
        tb.order(ByteOrder.nativeOrder());

        textureBuffer = tb.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

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
        loadTexture(SpaceGameRenderer.context, resID);
    }

    private int loadTexture(Context context, int resID){
        textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(), resID, options);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textures[0];
    }


    private void drawHelper(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        textureUniform = GLES20.glGetUniformLocation(mProgram,"u_TextureUnit");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(textureUniform, 0);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        textureBuffer.position(0);
        int aTextureHandle = GLES20.glGetAttribLocation(mProgram,"a_TextureCoordinates");
        GLES20.glVertexAttribPointer(aTextureHandle,2,GLES20.GL_FLOAT,false,8,textureBuffer);
        GLES20.glEnableVertexAttribArray(aTextureHandle);

        // get handle to fragment shader's vColor member
       /* mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle,COLOR_COMPONENTS_COUNT,
                GLES20.GL_FLOAT,false,
                COLOR_COMPONENTS_COUNT*4,colorBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);*/
        // Set color for drawing the triangle
        // GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // Draw the triangle
        GLES20.glEnable( GL_BLEND );
        GLES20.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
        //GLES20.glDrawArrays(GLES20.GL_LINES);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
      //  GLES20.glDisable(mColorHandle);
    }

    public void draw(float[] mMVPMatrix){
        float[] scratch = new float[16];
        float[] temp = new float[16];

        Matrix.setIdentityM(temp,0);
        Matrix.translateM(temp,0,translateX,translateY,translateZ);
        Matrix.scaleM(temp,0,scaleX,scaleY,scaleZ);
        Matrix.rotateM(temp, 0, rotateX, 1, 0, 0);
        Matrix.rotateM(temp, 0, rotateY, 0, 1, 0);
        Matrix.rotateM(temp, 0, rotateZ, 0, 0, 1);
        Matrix.multiplyMM(scratch,0,mMVPMatrix,0, temp,0);
        drawHelper(scratch);
    }

    public void translate(float x,float y,float z){
        translateX += x;
        translateY += y;
        translateZ += z;
    }

    public void changeTransform(float x,float y,float z){
        translateX = x;
        translateY = y;
        translateZ = z;
    }
    public void setDefaultTrans(float x,float y,float z){
        this.defTransX=x;
        this.defTransY=y;
        this.defTransZ=z;
        translateX = x;
        translateY = y;
        translateZ = z;
    }

    public void rotateX(float angle){
        if(rotateX+angle<=360) {
            rotateX += angle;
        }else{
            float temp = 360-rotateX;
            angle = angle - temp;
            rotateX = angle;
        }
    }

    public void rotateY(float angle){
        if(rotateY+angle<=360) {
            rotateY += angle;
        }else{
            float temp = 360-rotateY;
            angle = angle - temp;
            rotateY = angle;
        }
    }

    public void rotateZ(float angle){
        if(rotateZ+angle<=360) {
            rotateZ += angle;
        }else{
            float temp = 360-rotateZ;
            angle = angle - temp;
            rotateZ = angle;
        }
    }

    public void resetRotation(){
        rotateZ = 0f;
        rotateX =0f;
        rotateY =0f;
    }

    public void scale(float x, float y, float z){
        scaleX+=x;
        scaleY+=y;
        scaleZ+=z;
    }

    public void updateTransform(float x, float y, float z){
        this.translateX+=x;
        this.translateY+=y;
        this.translateZ+=z;
    }
}

