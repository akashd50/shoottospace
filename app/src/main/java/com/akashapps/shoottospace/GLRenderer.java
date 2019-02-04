package com.akashapps.shoottospace;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.opengl.GLES20;
//import android.opengl.Matrix;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.akashapps.shoottospace.Utilities.Utilities;
import com.akashapps.shoottospace.minigame1.LiftOffActivity;
//import com.threed.jpct.*;
//import com.threed.jpct.util.*;

public class GLRenderer implements GLSurfaceView.Renderer {
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    public static final float[] mMVPMatrix = new float[16];
    public static final float[] mProjectionMatrix = new float[16];
    public static final float[] mViewMatrix = new float[16];
    //public static
    private TouchController controller;
    public static Logger logger = Logger.getGlobal();
  /*  private World world;
    private FrameBuffer fb = null;
    private RGBColor back = new RGBColor(50, 50, 100);

    private Light sun, sun2;
    private Object3D cube, wall1,wall2, arena;
    private Object3D background,thing, square;

    private Object3D[] models;*/

    private ClickableIcon homeIcon;
    private Window window, spaceGameRenderer;

    public static Context context;
    private float statusBarHeight;
    public static float SCRWID, SCRHEIGHT, RATIO, screenTop,screenBottom;
    /*private Loader loader;*/
    //private Square s, sBack, example;
    private TexturedPlane tp;
/*
    private Object3D rickModel, road;
*/
    public static boolean DEV_MODE = false;
    private float csPrev;
    private boolean csBool;
    private LoadingScreen loadingScreen;
    //private int[] chars;
    //private TexturedPlane[] charTs;
    public static int FPS=0;
    private static long currentFrameTime, previousFrameTime;
    private Slider verticalSlider;

    private float currRickAngle, prevRickAngle;
    private DPad dpadMain;
/*
    private SkyBox skyBox;
*/
    private Window homeWindow;
    private boolean processesDone = false;
    public static boolean PAUSED = true;

    //private ObjDecoder cone;
    public GLRenderer(Context ctx, TouchController controller) {
        this.context = ctx;
        this.controller = controller;
        currentFrameTime = 0;
        previousFrameTime = 0;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        //TextureManager.getInstance().flush();
        //iniliazeUIElements();
        loadingScreen = new LoadingScreen(context);
        Utilities.initialzeTextBms();
        Utilities.setScreenVars(Utilities.getScreenWidthPixels()/Utilities.getScreenHeightPixels()
                ,Utilities.getScreenHeightPixels(), Utilities.getScreenWidthPixels());
        csPrev = 0f;
        csBool = false;
       // cone = new ObjDecoder(R.raw.cone_a, context);
        //initializeWorldBuffer((int)SCRWID, SCRHEIGHT);

    }

    private void iniliazeUIElements(){

        float[] col = {0.5f,0.0f,0.0f,0.5f};
        float[] col2 = {0.5f,0.5f,0.5f,0.5f};
        //homeWindow = new Window(Window.HOME_WINDOW,4,R.drawable.front,1.8f,1.0f,context);
        //homeWindow.setDefaultTrans(0f,0f,0f);
        //homeWindow.launched();
        //verticalSlider = new Slider(new SimpleVector(1.5f,0.0f,0.0f),Slider.VER_SLIDER,1.0f,context);
       // dpadMain = new DPad(new SimpleVector(-1.3f,-0.6f,0.0f),0.3f,context);

       // homeIcon = new ClickableIcon(R.mipmap.home_icon,0.3f,0.3f,context);
       // homeIcon.setDefaultTrans(-1.3f,0.7f,0.0f);
        //homeWindow.setHomeWinVars(dpadMain,verticalSlider);
       // homeWindow.addIcon(homeIcon);

       // window = new Window(Window.GENERAL_WINDOW,4,R.drawable.wind_i,2.5f,1.7f,context);
       // window.setDefaultTrans(0f,0f,1.0f);
       // homeIcon.setActivity(window);

        //ClickableIcon tW = new ClickableIcon(R.drawable.back_ic,0.2f,0.2f,context);
       // tW.setDefaultTrans(window.getRight()-0.2f-tW.getLength()/2,window.getRLCounter() - tW.getHeight()/2,1.5f);
        ///window.addIcon(tW);
       // window.setTerminatingIcon(tW, Window.R_LANE);

      //  ClickableIcon temp = new ClickableIcon(R.drawable.b_act,0.6f,0.3f,context);
        //temp.setDefaultTrans(window.getLeft()+temp.getLength()/2+0.1f,window.getLLCounter() - temp.getHeight()/2,1.5f);
       // temp.setDefaultTrans(0f,0f,1.5f);
        //window.addIconLeft(temp);
       // ClickableIcon temp2 = new ClickableIcon(R.drawable.b_act,0.6f,0.3f,context);
        //temp.setDefaultTrans(window.getLeft()+temp.getLength()/2+0.1f,window.getLLCounter() - temp.getHeight()/2,1.5f);
        //temp2.setDefaultTrans(0f,0f,1.5f);
       // window.addIconLeft(temp2);

        spaceGameRenderer = new SpaceGameRenderer(Window.FULL_SCREEN_ACTIVITY,2,R.drawable.wind_i,RATIO*2,2.0f,this.context);
        spaceGameRenderer.setDefaultTrans(0f,0f,-1f);
        spaceGameRenderer.setScaleX(1.2f);
        spaceGameRenderer.setScaleY(1.2f);
        ClickableIcon tacW = new ClickableIcon(R.drawable.back_ic,0.3f,0.3f,context);
        tacW.setDefaultTrans(spaceGameRenderer.getRight() - 0.2f,spaceGameRenderer.getTop() - 0.2f,1.0f);
        spaceGameRenderer.setTerminatingIcon(tacW, Window.R_LANE);
        spaceGameRenderer.launched();
        Window.addOpenWindow(spaceGameRenderer);
        //temp.setActivity(spaceGameRenderer);
        //temp.setSpecialActivity();

        //window.addIcon(temp);
        //window.setScaleX(2.0f);
        ;
        //controller.setDpadObjs(homeWindow);
    }

    public void onDrawFrame(GL10 unused) {
        previousFrameTime = System.nanoTime();
        //Logger.log("GLRENDERER Ratio: "+RATIO);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0f,0f,0f,1f);

        //drawJPCTStuff();
        customUIDrawing();

        currentFrameTime = System.nanoTime();
       long tTime = currentFrameTime - previousFrameTime;
       FPS = (int)(1000000000/tTime);
    }

    private void customUIDrawing(){
        android.opengl.Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f,
                0.0f, 0.0f, 0.0f,
                0f, 1.0f, 0.0f);
        android.opengl.Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

      /*  if(Utilities.TEXTURES_LOADED) {
            drawText("DIST: "+ collisionChecker.prevCollision(),new SimpleVector(-1.0f,-0.8f,1f), mMVPMatrix);
        }
*/
        if(processesDone) {
           /* float x = TouchController.TOUCHDOWNX;
            float y = TouchController.TOUCHDOWNY;
            if(x!=-1 && y!=-1) {
                homeWindow.onTouchDown(x, y);
                Window.checkWindows(TouchController.TOUCHDOWNX, TouchController.TOUCHDOWNY);
            }*/
            //homeWindow.ondrawFrame(mMVPMatrix);
            //window.ondrawFrame(mMVPMatrix);

            Window.drawWindows(mMVPMatrix);
           // cone.drawTriangles(mMVPMatrix);
        }else{
            loadingScreen.onDrawFrame(mMVPMatrix);

            if(controller.isFingerOnScreen()){loadingScreen.onTouchDown(TouchController.TOUCHDOWNX, TouchController.TOUCHDOWNY);}
            if(loadingScreen.isPlayTapped() && !controller.isFingerOnScreen()){
                processesDone = true;
                loadingScreen.free();
            }
           // drawText("SCRWID: "+SCRWID+" : ScrHI: "+SCRHEIGHT);
        }
    }

    /*public void drawJPCTStuff(){
        if(processesDone && !PAUSED) {
            controller.resetScrollFlags();
            if (controller.getRx() != 0) {
                cube.rotateY(controller.getRx());
                //thing.rotateY(controller.getRx());
                controller.resetRx();
            }

            if (controller.getRy() != 0) {
                cube.rotateX(controller.getRy());
                //thing.rotateX(controller.getRy());
                controller.resetRy();
            }

            Camera c = world.getCamera();

            if (dpadMain.isDirHor(TouchController.DPAD_DIR_R)) {
                //c.moveCamera(Camera.CAMERA_MOVERIGHT, controller.getActiveDpadX());
                rickModel.translate(dpadMain.getActiveDpadX(), 0, 0);
                //rickModel.rotateZ(1.57f);
                c.moveCamera(new SimpleVector(1, 0, 0), dpadMain.getActiveDpadX());
                c.lookAt(rickModel.getTransformedCenter());

            } else if (dpadMain.isDirHor(TouchController.DPAD_DIR_L)) {
                //c.moveCamera(Camera.CAMERA_MOVELEFT, controller.getActiveDpadX());
                rickModel.translate(-dpadMain.getActiveDpadX(), 0, 0);
                //rickModel.rotateZ(-1.57f);
                c.moveCamera(new SimpleVector(-1, 0, 0), dpadMain.getActiveDpadX());
                c.lookAt(rickModel.getTransformedCenter());
            }

            if (dpadMain.isDirVer(TouchController.DPAD_DIR_U)) {
                //c.moveCamera(Camera.CAMERA_MOVEUP, controller.getActiveDpadY());
                rickModel.translate(0, -dpadMain.getActiveDpadY(), 0);
                c.moveCamera(new SimpleVector(0, -1, 0), dpadMain.getActiveDpadY());
                c.lookAt(rickModel.getTransformedCenter());
            } else if (dpadMain.isDirVer(TouchController.DPAD_DIR_D)) {
                //c.moveCamera(Camera.CAMERA_MOVEDOWN, controller.getActiveDpadY());
                rickModel.translate(0, dpadMain.getActiveDpadY(), 0);
                c.moveCamera(new SimpleVector(0, 1, 0), dpadMain.getActiveDpadY());
                c.lookAt(rickModel.getTransformedCenter());
            }
            //c.align(square);

            if (verticalSlider.distanceFromOrigin() > 0) {
                csBool = true;
                csPrev = verticalSlider.distanceFromOrigin();
            } else if (verticalSlider.distanceFromOrigin() < 0) {
                csBool = false;
                csPrev = verticalSlider.distanceFromOrigin();
            }

            if (verticalSlider.distanceFromOrigin() > 0 || verticalSlider.distanceFromOrigin() < 0) {
                if (csBool) {
                    c.moveCamera(new SimpleVector(0f, -5f, 5f), 0.2f);
                    c.lookAt(rickModel.getTransformedCenter());
                } else {
                    c.moveCamera(new SimpleVector(0f, 5f, -5f), 0.2f);
                    c.lookAt(rickModel.getTransformedCenter());
                }
            }
            //if(s.getTransformX() != s.getDefaultX()){
            if (controller.isFingerOnScreen()) {
                TexturedPlane temp = dpadMain.getPad();
                double yy = (temp.getTransformY() - temp.getDefaultY());
                float xx = (temp.getTransformX() - temp.getDefaultX());
                double dPadAngle = 0f;
                if (xx < 0) {
                    dPadAngle = Math.atan(yy / xx);
                } else if (xx > 0) {
                    dPadAngle = Math.atan(yy / xx) + 3.14f;
                }
                rickModel.rotateZ((float) dPadAngle - prevRickAngle);
                prevRickAngle = (float) dPadAngle;
            }
            //road.rotateY(0.5f);
            //road.rotateX(0.1f);
            rickModel.checkForCollisionSpherical(new SimpleVector(1, 1, 2), 2);
            //skyBox.render(world,fb);
            fb.clear(back);
            world.renderScene(fb);
            world.draw(fb);
            fb.display();
        }
    }*/

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        SCRWID = width;
        SCRHEIGHT = height;
        RATIO = ratio;
        //Utilities.setScreenVars(ratio,height,width);
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        /*if(spaceGameRenderer.isOpened()) {
            android.opengl.Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 100);
        }else{*/
            android.opengl.Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        //android.opengl.Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        //}

        iniliazeUIElements();
        PAUSED = false;
        /*if (GLRendererActivity.activityManager== null) {
            Logger.log("Saving master Activity!");
            GLRendererActivity.activityManager = GLRendererActivity.instance;
        }*/
      //  initializeWorldBuffer(width,height);
        //PAUSED = false;
    }

    /*private void initializeWorldBuffer(int width, int height){
        if (fb != null) {
            fb.dispose();
        }
        //fb = new FrameBuffer(unused, width, height);
        fb =new FrameBuffer(width, height);

      if (GLRendererActivity.activityManager == null) {
       // if(!processesDone) {
           *//* world = new World();

            world.setAmbientLight(10, 20, 100);
            sun = new Light(world);
            sun.setIntensity(250, 250, 250);

            sun2 = new Light(world);
            sun2.setIntensity(100, 100, 255);
            //world.addObject(plane);
            //File f = new File("res/drawable-xxhdpi/ccol.jpg");
            //......................................................
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!TextureManager.getInstance().containsTexture("ccol.jpg")) {
                        loadTextures();
                    }
                    loadModels();
                    Camera cam = world.getCamera();
                    //cam.moveCamera( 50);
                    // cam.ro
                    cam.moveCamera(new SimpleVector(0, 5, -5), 30);
                    cam.lookAt(rickModel.getTransformedCenter());
                    SimpleVector sv = new SimpleVector();
                    sv.set(cube.getTransformedCenter());
                    sv.y -= 100;
                    sv.z -= 100;
                    sun.setPosition(sv);
                    sun2.setPosition(new SimpleVector(sv.x - 50, sv.y + 100, sv.z));
                    //MemoryHelper.compact();
                    PAUSED = false;
                    Logger.log("STUFF Loaded - ----------------------=========");
                    //processesDone = true;
                }
            }).start();*//*
            *//*if (GLRendererActivity.activityManager== null) {
                Logger.log("Saving master Activity!");
                GLRendererActivity.activityManager = GLRendererActivity.instance;
            }*//*
       }
    }

    private Object3D loadModel(int id1, float scale, String texture) {
        InputStream ir1 = context.getResources().openRawResource(id1);
        Object3D[] model = Loader.load3DS(ir1, scale);
        Object3D o3d = new Object3D(0);
        Object3D temp = null;
        for (int i = 0; i < model.length; i++) {
            temp = model[i];
            temp.setCenter(SimpleVector.ORIGIN);
            temp.rotateX((float)( -.5*Math.PI));
            temp.rotateMesh();
            temp.setRotationMatrix(new Matrix());
            o3d = Object3D.mergeObjects(o3d, temp);
            o3d.setTexture(texture);
            o3d.build();
        }
        return o3d;
    }*/

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }



   /* private void loadTextures(){
        InputStream image = context.getResources().openRawResource(R.raw.ccol);
        Texture t = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getDrawable(R.drawable.ccol)), 64, 64));
        TextureManager.getInstance().addTexture("ccol.jpg", t);

        Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(R.mipmap.ic_launcher)), 64, 64));
        TextureManager.getInstance().addTexture("texture", texture);

        Texture texture1 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(R.drawable.spaceback)), 128, 128));
        TextureManager.getInstance().addTexture("texture1", texture1);

        Texture rickT = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getDrawable(R.drawable.ricku)), 128, 128));
        TextureManager.getInstance().addTexture("ricku.jpg", rickT);

        Texture roadt= new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getDrawable(R.drawable.bftry)), 256, 256));
        TextureManager.getInstance().addTexture("bftry.jpg", roadt);

    }

    private void loadModels(){
        thing = loadModel(R.raw.cubecolored, 5, "ccol.jpg");
        thing.build();
        thing.translate(0,20,0);
        world.addObject(thing);

        road = loadModel(R.raw.roadt, 10,"bftry.jpg");
        road.build();
        road.rotateX(-1.57f);
        road.translate(0f,0f,-5f);
        world.addObject(road);

        arena = loadModel(R.raw.arena,20, "ccol.jpg");
        arena.build();
        arena.rotateX(-1.57f);
        world.addObject(arena);

        cube = Primitives.getCube(5);
        cube.calcTextureWrapSpherical();
        cube.setTexture("texture");
        cube.strip();
        cube.build();
        world.addObject(cube);

        rickModel = loadModel(R.raw.rfd,10, "ricku.jpg");
        rickModel.build();
        rickModel.translate(0,20,0);
        rickModel.rotateX(-1.57f);
        //rickModel.rotateZ(1.57f);
        prevRickAngle = 1.57f;
        world.addObject(rickModel);

        cube.addCollisionListener(collisionChecker);
        rickModel.addCollisionListener(collisionChecker);
        cube.enableCollisionListeners();
        rickModel.enableCollisionListeners();
        cube.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        rickModel.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
    }

    public void setUpReadingStream(){
        int id1 = R.raw.examplecube;
        int id2 = R.raw.examplecubemtl;

        InputStream ir1 = context.getResources().openRawResource(id1);
        InputStream ir2 = context.getResources().openRawResource(id2);

        models = Loader.loadOBJ(ir1,ir2,4);

        for(int i=0;i<models.length;i++){
            //models[i].calcTextureWrap();
            //models[i].setTexture(TextureManager.);
            models[i].build();
            models[i].translate(0f,20f,0f);
            //models[i].rotateX(180);
        }
    }
*/
    public static void drawText(String s, SimpleVector loc, float[] mMVPMatrix){
        float nl = loc.x;
        for(int i=0;i<s.length();i++){
            TexturedPlane temp = Utilities.CHARS_ARRAY[(int)s.charAt(i)];
            temp.changeTransform(nl,loc.y,loc.z);
            temp.draw(mMVPMatrix);
            nl+=0.1f;
        }
    }

    public void onActivityEnded(){
        /*processesDone = false;
        fb.clear();
        //fb.dispose();
        if(world!=null) {
            world.dispose();
        }
        TextureManager.getInstance().flush();
        PAUSED = true;*/
    }
}
