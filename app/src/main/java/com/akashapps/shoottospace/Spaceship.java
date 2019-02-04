package com.akashapps.shoottospace;


import android.content.Context;
import android.graphics.Color;
import android.opengl.Matrix;
import android.widget.Space;

public class Spaceship{
    private FanRect bulletShot = null;
    private static final float bulletVelY = 0.050f;
    private static final int NUM_BULLETS = 20;
    private FanRect[] shots;
    private int numBulletsAct = 0;
    private int bulletsGone = 0;
    private final int fireRate = 10;
    private int frCounter=0;

    private float currRotationAngle = 0f;
    private float targetAngle = 45f;

    private float targetRotationFrame, currMovingFrame;
    private boolean moveLeft,moveRight;
    public float velX, velY, fanRotation;
    //public float extVelX, extVelY;
    private int currUserShipLane;
    private Context context;

    public boolean isgettingHit;
    public boolean TOUCHING_SURFACE = false;
    public static float maxSpeed = 0.008f;
    public static float partialSpeedGain = 0.00005f;
    public static float BOOST_SPEED = 0.02f;
    public static float GROUND = 0.6f;
    public static float CLOSEST_GROUND_DIST = 99f;
    public static boolean PLATFORM_COLLISSION = false;


    private float x,y,z;
    private float[] color;
    private float length, height;
    //private ParticleSystem ps, gas;
    public boolean FLAME_ON;
    private boolean active, rotLeft, rotRight,rotForward,rotBackward;
    //private float leftWing[], rightWing[], front[];
    public boolean rickJumped = false, boostActive;
    public int fuelLevel, fuelMax;
    //private Square cSquare;
    public Splash splash;
    public TexturedPlane spaceship, fan1, fan2, right_flap, left_flap, spaceship2;
    public ParticleSystem p1, left, right;
    private static int RED = Color.rgb(225, 30, 30);
    private static int GREEN = Color.rgb(50, 30, 230);
    private static int BLUE = Color.rgb(50, 30, 255);
    private static int NEONISH = Color.rgb(20, 94, 191);
    //---------------------------------------------------

    public Spaceship(float l, float h, boolean player, Context context){
        //extVelX = 0f;
        //extVelY = 0f;

        boostActive = false;
        fuelMax = 2000;
        fuelLevel = fuelMax;
        targetRotationFrame = 10;
        moveLeft = false;
        moveRight = false;
        currUserShipLane = 1;
        currMovingFrame = 0.0f;
        this.context = context;
        FLAME_ON = false;
        x=0f; y=0f; z=0f;
        length = l;
        height = h;
        float[] c = {0.4f,0.4f,0.4f,1.0f};
        color = c;
        active = false;
        fanRotation = 0f;
        if(!player) {

        }else{
            //float[] lightLoc = {0.0f,0.0f,0f};
            spaceship = new TexturedPlane(length,height,context, R.drawable.rickspaceship);
            spaceship2 = new TexturedPlane(length,height,context, R.drawable.spaceship_wtr);
            spaceship.setDefaultTrans(0f,-0.8f,2f);
            spaceship2.setDefaultTrans(0f,-0.8f,2f);
            fan1 = new TexturedPlane(0.2f,0.2f,context,R.drawable.fan);
            fan2 = new TexturedPlane(0.2f,0.2f,context,R.drawable.fan);
            fan1.setDefaultTrans(spaceship.getDefaultX()-0.2f,-0.8f,1.5f);
            fan2.setDefaultTrans(spaceship.getDefaultX()+0.2f,-0.8f,1.5f);
            right_flap = new TexturedPlane(0.3f,0.3f,context,R.drawable.riglt_flap);
            left_flap = new TexturedPlane(0.3f,0.3f,context,R.drawable.left_flap);
            isgettingHit = false;
        }
        p1 = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0.05f,-0.5f,0f), BLUE, R.drawable.q_particle_iii, 50.0f,CustomParticles.LIGHT_BLEND, 20.0f, 2000, 0.03f, 2f);
        p1.setBlendType(Particle.LIGHT_BLEND);
        right = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0f,-0.1f,0f), Color.rgb(100, 99, 255),0,5.0f,CustomParticles.VN_BLEND, 5f, 300, 0f,0f);
        left = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0f,-0.1f,0f), Color.rgb(100, 99, 255),0,5.0f,CustomParticles.VN_BLEND, 5f, 300, 0f,0f);
        splash = new Splash(10);
        //  smoke = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector( 0.07f,0.07f,0f), Color.rgb(255, 255, 255), R.drawable.q_particle_v, 400, 200.0f, CustomParticles.VN_BLEND, 0.2f, 4f,-10f);

        velX = 0f; velY = 0f;
        // p2 = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0.03f,-0.2f,0f), Color.rgb(249, 200, 10), 500, 0.0f);
       // smoke = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0.08f,-0.7f,0f), Color.rgb(209, 207, 209), 500, 0.1f);
    }

    public void draw(float[] mvpMatrix){
        frCounter++;

        p1.onDrawFrame(mvpMatrix);
        left.onDrawFrame(mvpMatrix);
        right.onDrawFrame(mvpMatrix);

      //  smoke.onDrawFrame(mvpMatrix);

        velX -= velX+spaceship.rotateZ*0.001f;

        if(spaceship.transformX>=-GLRenderer.RATIO && spaceship.transformX<=GLRenderer.RATIO) {
            spaceship.updateTransform(velX, velY, 0f);
            spaceship2.updateTransform(velX, velY, 0f);
        }else {
            if(spaceship.transformX<-GLRenderer.RATIO) {
                spaceship.updateTransform(0.05f, velY, 0f);
                spaceship2.updateTransform(0.05f, velY, 0f);
                spaceship.rotateZ = 0f;
                spaceship2.rotateZ = 0f;
            }else if(spaceship.transformX>GLRenderer.RATIO){
                spaceship.updateTransform(-0.05f, velY, 0f);
                spaceship2.updateTransform(-0.05f, velY, 0f);
                spaceship.rotateZ = 0f;
                spaceship2.rotateZ = 0f;
            }
        }
        fan1.changeTransform(spaceship.getTransformX()-0.25f, spaceship.getTransformY(),spaceship.getTransformZ());
        fan2.changeTransform(spaceship.getTransformX()+0.25f, spaceship.getTransformY(),spaceship.getTransformZ());

        right_flap.changeTransform(spaceship.getTransformX()+0.1f, spaceship.getTransformY()+0.1f,spaceship.getTransformZ());
        left_flap.changeTransform(spaceship.getTransformX()-0.1f, spaceship.getTransformY()+0.1f,spaceship.getTransformZ());
        if(TouchController.fingerOnScreen) {
            if (spaceship.rotateZ > 0) {
                left_flap.draw(mvpMatrix);
            } else if (spaceship.rotateZ < 0) {
                right_flap.draw(mvpMatrix);
            }
        }

        fan1.draw(mvpMatrix);
        fan2.draw(mvpMatrix);

        if(rickJumped){
            spaceship2.draw(mvpMatrix);
        }else {
            spaceship.draw(mvpMatrix);
        }
        splash.ondrawFrame(mvpMatrix);
    }

    public void onTouchInput(DPad gamepad){

        if(gamepad.isClicked() && !rickJumped && fuelLevel>0) {
            this.FLAME_ON = true;
            //add particles to the particleSystem
            p1.addParticles(new SimpleVector(spaceship.transformX, spaceship.transformY, 2f), 30);
            left.addParticlesAndDirection(new SimpleVector((float)(spaceship.transformX-length/2), (float)(spaceship.transformY-0.1f), 2f),
                    new SimpleVector(velX, velY, 0f), 1);
            right.addParticlesAndDirection(new SimpleVector((float)(spaceship.transformX+length/2), (float)(spaceship.transformY-0.1f), 2f),
                    new SimpleVector(velX, velY, 0f), 1);

            if(velY<maxSpeed) {
            /*    smoke.addParticlesAndDirectionOL(new SimpleVector(spaceship.transformX, spaceship.transformY, 2f),
                        "x",new SimpleVector(0.2f,-0.6f,0f),1);
                smoke.addParticlesAndDirectionOL(new SimpleVector(spaceship.transformX, spaceship.transformY, 2f),
                        "x",new SimpleVector(-0.2f,-0.6f,0f),1);*/
                if(spaceship.transformY>SpaceGameRenderer.SURFACE){
                    TOUCHING_SURFACE = false;
                }
            }


            if(!boostActive) {
                if (velY < maxSpeed) {
                    velY += partialSpeedGain;
                    SpaceGameRenderer.score+=1;
                    if(fuelLevel>0){
                        fuelLevel-=1;
                    }
                }
            }else if(boostActive){
                //velY = BOOST_SPEED;
                SpaceGameRenderer.score+=10;
                if(fuelLevel>0){
                    fuelLevel-=10;
                }
            }


            if (gamepad.isDirHor(TouchController.DPAD_DIR_L)) {
                spaceship.rotateZ(0.1f);
            } else if (gamepad.isDirHor(TouchController.DPAD_DIR_R)) {
                spaceship.rotateZ(-0.1f);
            }

            if(fanRotation<30f){
                fanRotation+=1f;
            }
            fan1.rotateZ(fanRotation);
            fan2.rotateZ(fanRotation);

        }else{
            this.FLAME_ON = false;
            if(spaceship.rotateZ>0){
                // Logger.log("G>0------------");
                this.rotateZ(-0.1f);
            }else if(spaceship.rotateZ<0) {
                //Logger.log("L<0-------------");
                this.rotateZ(0.1f);
            }
            //if(SimpleVector.distanceXY(new SimpleVector()))
           // int locX = SpaceGameRenderer.groundar2[1].length/2 + (int)((spaceship.getTransformX()/GLRenderer.RATIO)*SpaceGameRenderer.groundar2[0].length/2);
           // if(locX>=0 && locX<SpaceGameRenderer.groundar2[1].length) {
            if(PlatformsContainer.base.onTriggerMidPtCollision(spaceship)){
                /*GROUND = PlatformsContainer.base.getDefaultY() + PlatformsContainer.base.getHeight() / 2 - SpaceGameRenderer.groundar2[1][locX];
                if (spaceship.getTransformY() - spaceship.getHeight() / 5 > GROUND) {
                    //this.updateTrasnform(0f,-SpaceGameRenderer.GRAVITY,0f);
                    if(velY>-SpaceGameRenderer.GRAVITY && !PLATFORM_COLLISSION) {
                        velY -= partialSpeedGain*5;
                    }
                } else {*/
                /*if(!TOUCHING_SURFACE){
                    splash.startSplashAnimation(new SimpleVector(spaceship.transformX, spaceship.transformY-spaceship.h/3,2f));
                }*/
                    TOUCHING_SURFACE = true;
                    velY = 0f;
                //}
            }else{
                if(velY>-SpaceGameRenderer.GRAVITY && !PLATFORM_COLLISSION) {
                    velY -= partialSpeedGain*5;
                }
            }

            if(spaceship.transformY>GROUND){
                left.addParticlesAndDirection(new SimpleVector((float)(spaceship.transformX-length/2), (float)(spaceship.transformY-0.1f), 2f),
                        new SimpleVector(velX, velY, 0f), 1);
                right.addParticlesAndDirection(new SimpleVector((float)(spaceship.transformX+length/2), (float)(spaceship.transformY-0.1f), 2f),
                        new SimpleVector(velX, velY, 0f), 1);

            }
            if(fuelLevel<fuelMax && !TouchController.fingerOnScreen) {
                fuelLevel += 10;
            }
            if(fanRotation>0){
                fanRotation-=0.1f;
            }
            fan1.rotateZ(fanRotation);
            fan2.rotateZ(fanRotation);


        }
    }

    public void drawEnemy(float[] mvpMatrix){

    }

    public boolean isClicked(float tx, float ty){
        if(spaceship.isClicked(tx,ty)) {
            return true;
        }else return false;
    }

    public boolean fire(){
        if(frCounter>=fireRate) {
            if (numBulletsAct >= NUM_BULLETS) {
                numBulletsAct = 0;
                for (int i = 0; i < NUM_BULLETS; i++) {
                    shots[i].deactivate();
                }
                bulletsGone = 0;
            }
            shots[numBulletsAct].activate();
            numBulletsAct++;
            frCounter = 0;

        }else{
            frCounter++;
        }
        return true;
    }

    private void drawBullets(float[] mMVPMatrix){
        if(numBulletsAct>0) {
            for(int i=bulletsGone;i<numBulletsAct;i++) {
                FanRect curr = shots[i];

                if(curr.isActive()) {
                    if (curr.getTransformY() + curr.getCY() >= 1.0f) {
                        curr.deactivate();
                        bulletsGone++;
                    }else {
                        float[] scratcht = new float[16];
                        float[] tempMoveMat = new float[16];
                        Matrix.setIdentityM(tempMoveMat, 0);
                        curr.updateTrasnformY(bulletVelY);
                        /*if(rotLeft) {
                            //if(currRotationAngle<targetAngle) currRotationAngle+=5;
                            Matrix.rotateM(tempMoveMat, 0, currRotationAngle, 0f, 1f, 0f);
                            //updateTrasnformX(0.1f);
                        }else if(rotRight){
                            //if(currRotationAngle>-targetAngle) currRotationAngle-=5;
                            Matrix.rotateM(tempMoveMat, 0, currRotationAngle, 0f, 1f, 0f);
                            //updateTrasnformX(-0.1f);
                        }*/
                        Matrix.rotateM(tempMoveMat, 0, 30f, 1f, 0f, 0f);

                        Matrix.translateM(tempMoveMat, 0, curr.getTransformX(), curr.getTransformY(), curr.getCZ());
                        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPMatrix, 0);
                        //bulletShot = bull;
                        curr.draw(scratcht);
                    }
                }
            }
        }
    }

    public boolean onTriggerCollision(Spaceship s){

        for(int i=0;i<NUM_BULLETS;i++){
            FanRect curr = shots[i];
            if(curr.isActive()){
               // float tx = (curr.getCX()+curr.getTransformX())*scrWidth/2 + scrWidth/2;
                // float ty = (float) (scrHeight/2-((curr.getCY()+curr.getTransformY())*scrHeight/2)/1.7);
                /*if(s.isClicked(tx,ty)){
                    return true;
                }*/
            }
        }
        return false;
    }

    public void updateTrasnform(float x, float y, float z){
        spaceship.updateTransform(x,y,z);
        spaceship2.updateTransform(x,y,z);
    }

    public void setDefaultTrans(float x, float y, float z){
        spaceship.setDefaultTrans(x,y,z);
        spaceship2.setDefaultTrans(x,y,z);

    }
    public void changeTrasnform(float x, float y, float z){
        spaceship.changeTransform(x,y,z);
        spaceship2.changeTransform(x,y,z);
    }

    public float getTransformX(){return spaceship.getTransformX();}
    public float getTransformY(){return spaceship.getTransformY();}

    public boolean isActive(){return this.active;}
    public void activateBoost(){
        boostActive = true;
        velY = BOOST_SPEED;
    }
    public void deactivateBoost() {
        boostActive = false;
        velY = maxSpeed;
    }

    public void scale(float x, float y){
        spaceship.scale(x,y);
        spaceship2.scale(x,y);
        fan1.scale(x,y);
        fan2.scale(x,y);
        //left_flap.scale(x,y);
    }

    public void rotateZ(float angle){
        spaceship.rotateZ(angle);
        spaceship2.rotateZ(angle);
        //currRotationAngle = 0f;
    }
    public void rotateRight(){
        this.rotRight = true;
        rotLeft = false;
        //currRotationAngle = 0f;
    }
    public void resetRotation(){
        rotRight = false;
        rotLeft = false;
        rotForward = false;
        rotBackward = false;
        currRotationAngle = 0f;
    }
    public float getRotateZ(){return spaceship.rotateZ;}
    public boolean getRotateX(){return rotRight;}
    public void rotateForward(){
        this.rotForward = true;
        if(rotBackward){
            currRotationAngle = 0.0f;
            this.rotBackward = false;
        }

    }
    public void rotateBackward(){
        this.rotBackward = true;
        if(rotForward){
            currRotationAngle = 0.0f;
            this.rotForward = false;
        }
    }

    public void moveLeft(){

        if(currUserShipLane==0) {

        }else{
            moveRight = false;
            //rotLeft = true;
            currMovingFrame = 0f;
            currUserShipLane -= 1;
            moveLeft = true;

            //this.transformX = shipLocsX[currUserShipLane];
        }
    }

    public int getCurrUsershipLane(){
        return currUserShipLane;
    }

    public void moveRight(){
        if(currUserShipLane==2) {
        }else{
                moveLeft = false;
                //rotRight = true;
                currMovingFrame = 0f;
                currUserShipLane += 1;
                moveRight = true;
                //this.transformX = shipLocsX[currUserShipLane];
            }
    }

    public void reactivateFlame(){
      //  ps.reactivateParticles(spaceship.rotateZ);
    }

    public void setFLAME_ON(boolean fl){
        FLAME_ON = fl;
    }
}
