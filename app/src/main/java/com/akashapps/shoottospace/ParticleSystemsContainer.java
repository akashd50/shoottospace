package com.akashapps.shoottospace;

import android.graphics.Color;

public class ParticleSystemsContainer {
    private Meteor[] meteors;
    private Meteor[] bbmeteors;
    //private EMeteor em;
    private int frameCounter, waitingFrames, bgMeteorCounter;
    private ParticleSystem boxParticles;
    private float particleOpacity;
    public ParticleSystemsContainer(){
        waitingFrames = 600;
        frameCounter = 0;
        bgMeteorCounter = 0;
        meteors = new Meteor[5];
        particleOpacity = 1f;
        meteors[0] = new Meteor(true, new SimpleVector(1.88f,1f,1f),0.2f, 3.14f);
        meteors[1] = new Meteor(true, new SimpleVector(-1.88f,0.6f,1f),0.2f, 0f);
        meteors[2] = new Meteor(true, new SimpleVector(1.88f,1.2f,1f),0.2f, 3.14f);
        meteors[3] = new Meteor(true, new SimpleVector(1.9f,0.8f,1f),0.15f, 3.14f);
        meteors[4] = new Meteor(true, new SimpleVector(1.88f,0.6f,1f),0.1f, 3.14f);
        //em = new EMeteor(new SimpleVector(1.88f,(float)Math.random()*1.2f,1f),(float)Math.random()*0.25f, 3.14f, 7);
        for(int i=0;i<5;i++){
            meteors[i].active = false;
        }
        activateRandom(3,meteors);
    }

    public void readyBGMeteors(){
        bbmeteors = new Meteor[10];
        for(int i=0;i<10;i++){
            bbmeteors[i] = new Meteor(true, new SimpleVector(1.88f,(float)Math.random()*1.2f,1f),(float)Math.random()*0.25f, 3.14f, 2);
            bbmeteors[i].active = false;
        }
        activateRandom(3,bbmeteors);
    }

    public void readyBGParticles(){
        boxParticles = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0.2f,0.2f,0f),
                Color.rgb(200,60,70), R.drawable.q_particle_iii, 25f, CustomParticles.LIGHT_BLEND, 0, 2000,0f,0f);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boxParticles.addParticlesBoxParticleSystem(GLRenderer.RATIO*2,2.0f,2000);
            }
        }).start();
    }

    public void onDrawFrameMeteors(float[] mMVPMatrix){
        if(frameCounter<waitingFrames){
            frameCounter++;
        }
        else{
            frameCounter=0;
            activateRandom(2, this.meteors);
        }

        for(int i=0;i<5;i++){
            //if(meteors[i].active){

                meteors[i].onDrawFrame(mMVPMatrix);
           //}
        }
       // em.onDrawFrame(mMVPMatrix);
    }

    public void onDrawFrameBGMeteors(float[] mMVPMatrix){
        if(bgMeteorCounter<waitingFrames){
            bgMeteorCounter++;
        }
        else{
            bgMeteorCounter=0;
            activateRandom(4, bbmeteors);
        }

        for(int i=0;i<5;i++){
            //if(meteors[i].active){
            bbmeteors[i].onDrawFrame(mMVPMatrix);
            //}
        }
    }

    public void onDrawFrameBGP(float[] mMVPMatrix){
        if(particleOpacity>0){
            particleOpacity-=0.005;
        }
        boxParticles.addParticlesBoxParticleSystem(GLRenderer.RATIO*2,2.0f,2);
        boxParticles.onDrawFrame(mMVPMatrix, particleOpacity);
    }

    public void checkCollisionsMeteors(Spaceship ship){
        final Spaceship s = ship;
        new Thread(new Runnable() {
            @Override
            public void run() {
                TexturedPlane tp = s.spaceship;
                for(int i=0;i<meteors.length;i++){
                    if(meteors[i].active) {
                        float x = meteors[i].trX;
                        float y = meteors[i].trY;
                        if (tp.onTriggerCollisionPoint(x, y)) {
                            if (x > tp.transformX) {
                                s.rotateZ(1);
                                s.velY -= 0.001;
                                //s.extVelX+= 0.01f;
                            } else {
                                s.rotateZ(-1);
                                // s.extVelX-=0.01f;
                                s.velY -= 0.001;
                            }
                            s.isgettingHit = true;
                            SpaceGameRenderer.hitCounter+=1;
                        }else{
                            s.isgettingHit = false;
                        }
                    }
                }
            }
        }).start();
    }

    public void checkCollisionsMeteorsWtPlatform(PlatformsContainer p){
        for(int i=0;i<meteors.length;i++) {
            if (meteors[i].active) {
                p.onTriggerCollisionPoint(meteors[i]);
            }
        }
    }

    private void activateRandom(int num, Meteor[] meteors){
        for(int i=0;i<num;i++){
            int r = (int)(Math.random()*5);
            meteors[r].reactivateWithNewLoc(1.9f, SpaceGameRenderer.cameraY+0.5f);
        }
    }

    public Meteor getMeteor(int i){
        return meteors[i];
    }
}
