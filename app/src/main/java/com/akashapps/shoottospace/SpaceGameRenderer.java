package com.akashapps.shoottospace;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.method.Touch;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SpaceGameRenderer extends Window{
    private Window gameWindow;
    private TextDecoder textDecoder;
    //private TexturedPlane background, layout;
    public static float GRAVITY = 0.0098f;
    public static float SHIPSPEED = 0.002f;
    public static int score = 0;
    public static int hitCounter=0;
    public Spaceship spaceship;
    private DPad gamepad;
    private int counter;
    public static float SURFACE = -0.6f;
    public static float TOP = 0.6f;
    //private Meteor/* m1,*/back_mt1, m2;
    private ParticleSystem particleSystem;
    //public static TexturedPlane base;
    private TexturedPlane background, crash, abandon_ic, rev_anim_ic,
            city_bgc, fuelBar, fuelBarFuel, frontScreen, redScreen;
    private ClickableIcon boost;
   // private boolean abandon_an;
   // private Animation rickJumps, rickWalking_l, rickWalking_r;
    private CloudsContainer cloudsContainer;

    private static float[] mmMVPMatrix = new float[16];
    private float cloudSpeed, cloud2speed;
    public static float cameraX, cameraY;
    public static Context context = null;
    protected boolean isLoaded;
    private int[][] groundar;
    public static float[][] groundar2;
    Mesh meteorMesh;
   // private Circle circle;
    //private ObjDecoder cube;
   // private TexturedCylinder cylinder;
    private Collectables collectables;
    private ParticleSystemsContainer psc;
    private PlatformsContainer platformsContainer;
    //private ParticleSystem ps;
    private Turret turret;
    public static float[] sunlight = {0.4f,0.3f,0.2f,1.0f};
    public static float[] reddishTint = {0.9f,0.5f,0.6f,1.0f};
    private float[] testLightloc;
    private float lastFrameTime, currentFrameTime;
    //private Splash splash;
    private static float SCENE_SCALE = 1f;
    DatabaseReference myRef;

    public SpaceGameRenderer(String t, int s, int bgID, float l, float h, Context context){
        super(t,s,bgID,l,h,context);
        this.context = GLRenderer.context;
        float[] colorA = {1.0f,0f,0f,1f};
       // a = new TextDecoder(0.3f,0.3f,R.drawable.a, colorA);
       // a.setDefaultTrans(0f,0f,2f);

        lastFrameTime = 0f;
        currentFrameTime = 0f;
        score = 0;
        hitCounter = 0;
        isLoaded = false;
        gameWindow = this;
        textDecoder = new TextDecoder();
       // rickJumps = new Animation(4,5, false);
        //rickWalking_l = new Animation(4,10,true);
       // rickWalking_r = new Animation(4,10,true);
        //circle = new Circle(new SimpleVector(0f,0f,2f),1,0.5f);
       // cylinder = new TexturedCylinder(new SimpleVector(0f,0f,0f), 36,0.2f,0.3f, R.drawable.cylinder_tex_i);
        //cylinder.setDefaultTrans(0f,0f,2f);
       // cylinder.translate(0f,0f,2f);
        collectables = new Collectables(20);
        boost = new ClickableIcon(R.drawable.boost_light, R.drawable.boost_dark, 0.4f,0.4f,context);
        boost.setDefaultTrans(1.5f,0.2f,2f);

        psc = new ParticleSystemsContainer();
        platformsContainer = new PlatformsContainer();
        cloudsContainer = new CloudsContainer();
        psc.readyBGMeteors();
        turret = new Turret(new SimpleVector(1.0f,-0.6f,2f));
        //cube = new ObjDecoder(R.raw.textured_n_cube, R.drawable.num_texture,context);
       // cube.scale(-0.5f,-0.5f);
        initializeWindow();
    }

    @Override
    public void initializeWindow() {
        spaceship = new Spaceship(0.7f,0.4f,true,context);
        spaceship.setDefaultTrans(0f,0f,2f);
        //spaceship.scale(3.0f,3.0f);
        spaceship.spaceship.getSquaredArray(20,2,R.drawable.rickspaceship,0.7f,0.4f);
        float[][] t = spaceship.spaceship.collisionCoords;
        for(int i=0;i<t[0].length;i++){
            Log.v("SPACESHIP:","X: "+t[0][i]+" Y1: "+t[1][i]+" Y2: "+t[2][i]);
        }
        groundar2 = platformsContainer.base.getHeightArray(5, R.drawable.base_r, GLRenderer.RATIO*2f,0.4f);

        platformsContainer.base.getSquaredArray(10, 2,R.drawable.base_r,GLRenderer.RATIO*2f, 0.4f);

        gamepad = new DPad(new SimpleVector(-1.0f,-0.6f,2.0f), 0.3f,context);
        //m1 = new Meteor(R.drawable.pix_meteor_ii, R.drawable.meteor_back_i, R.drawable.meteor_back_ii, context);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("score");
        myRef.addListenerForSingleValueEvent(postListener);

        /*float[] basell = {0.0f,0.5f,0f};
        base = new TexturedPlane(GLRenderer.RATIO*2f, 0.4f,context,R.drawable.base_r, reddishTint, basell);
        base.scale(3.0f,3.0f);*/

        //platformsContainer.base.getSquaredArray(20, 4, R.drawable.base_r, GLRenderer.RATIO*2f,0.4f);

        meteorMesh = new Mesh(platformsContainer.platforms[0].collisionCoords);
        crash = new TexturedPlane(0.4f, 0.4f,context,R.drawable.crash);
        crash.setDefaultTrans(0f,-0.7f,2f);

        float[] lightLoc = {0f,-3.0f,0f};
        testLightloc = lightLoc;
        background = new TexturedPlane(GLRenderer.RATIO*4f+0.2f, 12.0f,context,R.drawable.bacground, reddishTint, lightLoc);
        background.setDefaultTrans(0f,3f,0f);
        abandon_ic = new TexturedPlane(0.5f,0.2f,context, R.drawable.abandon_ic);
        rev_anim_ic = new TexturedPlane(0.5f,0.2f,context, R.drawable.abandon_ic);
        rev_anim_ic.setDefaultTrans(1.5f,-0.8f,2f);
        abandon_ic.setDefaultTrans(1.5f,-0.5f,2f);

        float[] cityLightLoc = {0.5f,-0.2f,2f};
        city_bgc = new TexturedPlane(GLRenderer.RATIO*2,2.0f,context,R.drawable.bgc_city_ii, reddishTint, cityLightLoc);
        city_bgc.setDefaultTrans(0f,0f,1f);


       /* rickJumps.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.rickfallsr_i));
        rickJumps.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.rickfallsr_ii));
        rickJumps.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.rickfallsr_iii));
        rickJumps.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.rickfallsr_iv));
        rickJumps.changeTransform(0.3f,0f,2f);*/

        /*explosion.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.liftoff_i));
        explosion.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.liftoff_ii));
        explosion.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.liftoff_iii));
        explosion.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.liftoff_iv));
        explosion.addFrame(new TexturedPlane(0.6f, 0.4f,context,R.drawable.liftoff_v));
        explosion.addFrame(new TexturedPlane(0.7f, 0.5f,context,R.drawable.liftoff_vi));
        explosion.addFrame(new TexturedPlane(0.8f, 0.6f,context,R.drawable.liftoff_vii));
        explosion.addFrame(new TexturedPlane(0.9f, 0.7f,context,R.drawable.liftoff_viii));
        // explosion.addFrame(new TexturedPlane(0.8f, 0.6f,context,R.drawable.liftoff_vii));
        // explosion.addFrame(new TexturedPlane(1.0f, 0.8f,context,R.drawable.liftoff_viii));
        explosion.changeTransform(0f,0f,2f);*/

       /* rickWalking_l.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_i));
        rickWalking_l.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_ii));
        rickWalking_l.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_iii));
        rickWalking_l.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_iv));
        rickWalking_l.changeTransform(spaceship.getTransformX()+0.4f,SURFACE,2f);

        rickWalking_r.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_r_i));
        rickWalking_r.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_r_ii));
        rickWalking_r.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_r_iii));
        rickWalking_r.addFrame(new TexturedPlane(0.2f,0.4f,context,R.drawable.walking_r_iv));
        rickWalking_r.changeTransform(spaceship.getTransformX()+0.4f,SURFACE,2f);*/

        frontScreen = new TexturedPlane(2*GLRenderer.RATIO, 2.0f,context,R.drawable.front_screen_i_white);
        frontScreen.setDefaultTrans(0f,0f,2.2f);

        redScreen = new TexturedPlane(2*GLRenderer.RATIO, 2.0f,context,R.drawable.front_screen_i_red);
        redScreen.setDefaultTrans(0f,0f,2.2f);

        fuelBar = new TexturedPlane(0.15f,0.6f,context,R.drawable.fuel_bar);
        fuelBarFuel = new TexturedPlane(0.15f,0.6f,context,R.drawable.fuel_bar_fuel);
        //fuelBarFuel.setScaleY((0.6f/0.01f)*0.01f);
        fuelBarFuel.setDefaultTrans(0f,0.7f,2f);
        fuelBar.setDefaultTrans(0f,0.7f,2f);
        fuelBarFuel.rotateZ = -90;
        fuelBar.rotateZ = -90;

        particleSystem = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0.0f,0.5f,0f),
                Color.rgb(200,50,60),R.drawable.q_particle_v,25f,
                CustomParticles.VN_BLEND, 10, 1000,0f,2f);
        isLoaded = true;
       // super.initializeWindow();
    }

    @Override
    public void onTouchDown(float x, float y) {
        if(this.isOpened()) {
            gamepad.onTouchDown(x, y);
            /*if(gamepad.isClicked() && spaceship.rickJumped){
                rickWalking_l.startAnimation();
                rickWalking_r.startAnimation();
            }*/
            if(abandon_ic.isClicked(x,y)){
                //abandon_an = true;
                //rickJumps.startAnimation();
                //explosion.startAnimation();
                spaceship.rickJumped = true;
                turret.active = true;
               /* flameBack.startAnimation();
                flame.startAnimation();*/
               /* if(!thruster.active) {
                    thruster.startAnimation();
                }else{
                    thruster.active = false;
                }*/
            }else if(rev_anim_ic.isClicked(x,y)){
             //   rickJumps.startAnimationRev();
                turret.active = false;
            }else if(boost.isClicked(x,y)){
                spaceship.boostActive = true;
            }
            turret.onTouchDown(x,y);
            super.onTouchDown(x,y);
        }
    }

    @Override
    public void onSecondaryTouchDown(float x, float y) {
        if(boost.isClicked(x,y)){
            spaceship.activateBoost();
        }
        turret.onTouchDown(x,y);
    }

    @Override
    public void onSecondaryTouchUp(float x, float y) {
       // super.onSecondaryTouchUp(x, y);
        if(boost.isClicked){
            spaceship.deactivateBoost();
            boost.onTouchUp();
        }
    }

    @Override
    public void onTouchMove(float x, float y) {
        gamepad.onTouchMove(x,y);
    }

    @Override
    public void onTouchUp(float x, float y) {
        gamepad.onTouchUp(x,y);
        if(boost.isClicked) {
            spaceship.deactivateBoost();
            boost.onTouchUp();
        }
    }

    @Override
    public void launched() {
        super.launched();
        //particles.startAnimation();
    }

    @Override
    public void ondrawFrame(float[] mMVPMatrix) {
        cameraX = 0f;
        if(spaceship.spaceship.transformY>0f) {
            cameraY = spaceship.spaceship.transformY;
        }else{
            cameraY = 0f;
        }

        if(spaceship.velY>spaceship.maxSpeed/2 && SCENE_SCALE>0.5f){
            spaceship.spaceship.scaleX = SCENE_SCALE;
            spaceship.spaceship.scaleY = SCENE_SCALE;
            background.scaleX = SCENE_SCALE;
           // platformsContainer.onUpdateFrame(SCENE_SCALE);
            //platformsContainer.setScale(SCENE_SCALE,SCENE_SCALE);
            SCENE_SCALE-=0.001f;
        }else if(spaceship.velY<spaceship.maxSpeed/2 && SCENE_SCALE<1.0f){
            spaceship.spaceship.scaleX = SCENE_SCALE;
            spaceship.spaceship.scaleY = SCENE_SCALE;
            background.scaleX = SCENE_SCALE;
           // platformsContainer.onUpdateFrame(SCENE_SCALE);
            //platformsContainer.setScale(SCENE_SCALE,SCENE_SCALE);
            SCENE_SCALE+=0.001f;
        }

        android.opengl.Matrix.setLookAtM(GLRenderer.mViewMatrix, 0, 0, cameraY, 5.0f,
               0f, cameraY, 0.0f,
                0f, 1.0f, 0.0f);
        android.opengl.Matrix.multiplyMM(this.mmMVPMatrix, 0, GLRenderer.mProjectionMatrix, 0, GLRenderer.mViewMatrix, 0);

        spaceship.onTouchInput(gamepad);
        drawEnvironment();

        /*if(spaceship.spaceship.scaleX>1.0f){
            spaceship.scale(-0.01f,-0.01f);
        }*/
        collectables.onDrawFrame(mmMVPMatrix, spaceship);

        spaceship.draw(mmMVPMatrix);
      //  animateRick(mmMVPMatrix);
        drawUI(mMVPMatrix);

        psc.checkCollisionsMeteors(spaceship);
        platformsContainer.onTriggerCollision(spaceship);
        psc.checkCollisionsMeteorsWtPlatform(platformsContainer);
        turret.onDrawFrame(mmMVPMatrix);
        if(turret.active){
            turret.onTouchInput(gamepad);
        }
        turret.onDrawFrameStatic(mMVPMatrix);
        if(spaceship.isgettingHit){
            redScreen.draw(mMVPMatrix);
        }else {
            frontScreen.draw(mMVPMatrix);
        }
        meteorMesh.drawMesh(mMVPMatrix);
        //GLRenderer.drawText(""+score,new SimpleVector(-1.6f,0.8f,2f),mMVPMatrix);

        //GLRenderer.drawText("HITS: "+hitCounter, new SimpleVector(0.6f,0f,2f), mMVPMatrix);
        textDecoder.drawText("HITS: "+hitCounter, new SimpleVector(0.6f,0f,2f), mMVPMatrix);

       // GLRenderer.drawText("VELX: "+spaceship.velX, new SimpleVector(-1.6f,0.6f,2f), mMVPMatrix);
      //  GLRenderer.drawText("VELY: "+spaceship.velY, new SimpleVector(-1.6f,0.4f,2f), mMVPMatrix);
        if(GLRenderer.DEV_MODE){
            drawDEVStuff(mMVPMatrix);
        }

        textDecoder.drawText("SCORE "+score,new SimpleVector(-1.6f,0.8f,2f), new SimpleVector(1.2f,1.4f,1f), mMVPMatrix);

        /*float[] color = {(float)Math.random(),(float)Math.random(),(float)Math.random(),1f};
        a.draw2(mMVPMatrix, color);*/
        /*if(TouchController.rotationalTurnY!=0) {
            cube.rotateX(TouchController.rotationalTurnY);
            TouchController.rotationalTurnY = 0;
        }
        if(TouchController.rotationTurnX!=0) {
            cube.rotateY(TouchController.rotationTurnX);
            TouchController.rotationTurnX = 0;
        }
        //cube.rotateZ(0.5f);
        cube.onDrawFrame(mMVPMatrix);*/

    }

    private void updateGameTransforms(){

    }

    private void drawDEVStuff(float[] mMVPMatrix){
        GLRenderer.drawText("CAMERAX: "+cameraX, new SimpleVector(0.6f,0.4f,2f),mMVPMatrix);
        GLRenderer.drawText("CAMERAY: "+cameraY, new SimpleVector(0.6f,0.2f,2f),mMVPMatrix);
        GLRenderer.drawText("FPS: "+GLRenderer.FPS,new SimpleVector(-1.6f,0.0f,2f), mMVPMatrix);
    }
    private void drawUI(float[] mMVPMatrix){
        gamepad.draw(mMVPMatrix);
        abandon_ic.draw(mMVPMatrix);
        rev_anim_ic.draw(mMVPMatrix);
        if(spaceship.fuelLevel<spaceship.fuelMax){
            float scale = (float)spaceship.fuelLevel/(float)spaceship.fuelMax;
            fuelBarFuel.setScaleX(scale);
            fuelBarFuel.transformX= (-0.6f*(1.0f-scale))/2;
        }
        fuelBarFuel.draw(mMVPMatrix);
        fuelBar.draw(mMVPMatrix);
        boost.draw(mMVPMatrix);
        super.ondrawFrame(mMVPMatrix);

    }

    private void drawEnvironment(){
        background.draw(mmMVPMatrix);
        if(platformsContainer.base.scaleX>1.0f){
            platformsContainer.base.scale(-0.01f,-0.01f);
        }
        psc.onDrawFrameBGMeteors(mmMVPMatrix);//------------------------------BGMeteors-------------------------
        cloudsContainer.onUpdateFrame();
        cloudsContainer.drawCylinderCloud(mmMVPMatrix);
        if(spaceship.velY>0 && cameraY>0) {
          //  city_bgc.updateTransform(0f, Spaceship.maxSpeed * 0.40f, 0f);
            background.updateTransform(0f, Spaceship.maxSpeed * 0.70f, 0f);
            cloudsContainer.c_cloud.updateTransform(0f, Spaceship.maxSpeed * 0.50f, 0f);
        }
       /* if(spaceship.velY<0 && city_bgc.transformY>=city_bgc.getDefaultY() && cameraY>=0){
            city_bgc.updateTransform(0f, -Spaceship.maxSpeed * 0.40f, 0f);
            //background.updateTransform(0f, -Spaceship.maxSpeed * 0.70f, 0f);
        }*/
        if(spaceship.velY<0 && background.transformY>=background.getDefaultY() && cameraY>=0){
            background.updateTransform(0f, -Spaceship.maxSpeed * 0.70f, 0f);
        }
        if(spaceship.velY<0 && cloudsContainer.c_cloud.translateY>=cloudsContainer.c_cloud.defTransY && cameraY>=0){
            cloudsContainer.c_cloud.updateTransform(0f, -Spaceship.maxSpeed * 0.50f, 0f);
        }
        city_bgc.draw(mmMVPMatrix);

        cloudsContainer.onDrawFrameBG(mmMVPMatrix);
        cloudsContainer.onDrawFrameFG(mmMVPMatrix);
        psc.onDrawFrameMeteors(mmMVPMatrix);
        platformsContainer.onDrawFrameFG(mmMVPMatrix);
        crash.draw(mmMVPMatrix);
    }

   /* private void updateBase(float[] mMVPMatrix){
        if(gamepad.isClicked()) {
            base.updateTransform(0f, -0.002f, 0f);
            crash.updateTransform(0f,-0.002f,0f);
            background.updateTransform(0f,-0.002f,0f);
        }else if(!gamepad.isClicked() && base.getTransformY()+base.getHeight()/2<SURFACE){
            base.updateTransform(0f,GRAVITY,0f);
            crash.updateTransform(0f,GRAVITY,0f);
            background.updateTransform(0f,GRAVITY,0f);
        }

    }*/

    /*private void animateRick(float[] mMVPMatrix){
        if(rickJumps.active) {
            rickJumps.changeTransform(spaceship.getTransformX()+0.3f, spaceship.getTransformY(), 2f);
            rickJumps.onDrawFrame(mMVPMatrix);

        }else if(rickJumps.activeRev){
            rickJumps.changeTransform(spaceship.getTransformX()+0.3f, spaceship.getTransformY(), 2f);
            rickJumps.onDrawFrameRev(mMVPMatrix);
            if(rickJumps.currFrameRev==0){
                spaceship.rickJumped = false;
            }
        }
        *//*if (gamepad.isDirHor(TouchController.DPAD_DIR_L) && spaceship.rickJumped) {
            // Logger.log("LEFTTTTT----------");
            rickWalking_l.onDrawFrame(mmMVPMatrix);
            rickWalking_l.updateTransform(-gamepad.getActiveDpadX()/50,0f,0f);
            rickWalking_r.updateTransform(-gamepad.getActiveDpadX()/50,0f,0f);

        } else if (gamepad.isDirHor(TouchController.DPAD_DIR_R)&& spaceship.rickJumped) {
            // Logger.log("RIGHTTTT----------");
            rickWalking_r.onDrawFrame(mmMVPMatrix);
            rickWalking_l.updateTransform(gamepad.getActiveDpadX()/50,0f,0f);
            rickWalking_r.updateTransform(gamepad.getActiveDpadX()/50,0f,0f);
        }else{
            if(gamepad.PREVIOUS_DIR_HOR!=null) {
                if (gamepad.PREVIOUS_DIR_HOR.compareTo(TouchController.DPAD_DIR_R) == 0) {
                    rickWalking_r.drawStillFrame(mMVPMatrix);
                }else{
                    rickWalking_l.drawStillFrame(mMVPMatrix);
                }
            }else{
                rickWalking_r.drawStillFrame(mMVPMatrix);
            }
        }
        int locX = groundar2[1].length/2 + (int)((rickWalking_l.getTransformX()/GLRenderer.RATIO)*groundar2[0].length/2);
        float ground = base.getDefaultY() + base.getHeight()/2 - groundar2[1][locX];
        if(rickWalking_l.frames[0].transformY - rickWalking_l.frames[0].getHeight()/3>ground){
            rickWalking_l.updateTransform(0f,-GRAVITY,0f);
            rickWalking_r.updateTransform(0f,-GRAVITY,0f);
        }else if(rickWalking_l.frames[0].transformY - rickWalking_l.frames[0].getHeight()/3<ground-GRAVITY){
            rickWalking_l.updateTransform(0f,GRAVITY,0f);
            rickWalking_r.updateTransform(0f,GRAVITY,0f);
        }*//*

        // explosion.onDrawFrame(mMVPMatrix);
    }*/

    @Override
    public void onActivityQuit() {
        myRef.setValue(score);

        spaceship.scale(3.0f,3.0f);
        platformsContainer.base.scale(3.0f,3.0f);

        super.onActivityQuit();
    }
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        }

        public void addListenerForSingleValueEvent(){

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public static float getGLTexturedCoordX(float x, float len){
        return (1.0f/2f + (x/GLRenderer.RATIO)*(1.0f/2f));
    }

    public static float getGLTexturedCoordY(float y){
        return (1.0f/2f - (y/1.0f)*(1.0f/2f));
    }

}
