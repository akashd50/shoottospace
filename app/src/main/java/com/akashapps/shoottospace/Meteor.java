package com.akashapps.shoottospace;

import android.content.Context;
import android.graphics.Color;
import javax.microedition.khronos.opengles.GL;

public class Meteor {
    private float fireAlpha;
    public static Context context = null;
    private float  pathAngle, scaling;
    private int frameCounter, frameCounter2;
    public boolean active, fireactive, platformCollision;
    private int targetFrames = 300;
    public TexturedPlane meteor_front, meteor_surr1, meteor_surr2, meteor_wt, mblue;
    private float initX, initY, angle, initVel, velX, velY;
    public static float GRAVITY = 0.0098f;
    public Animation mt_explosion;
    private boolean isParticleMt;
    private ParticleSystem m, pt_explosion, overhead;
    public float trX, trY, trZ;
    private static int fireFrames = 600;
    private int numPart, delay, counter;
    private int fireCounter = 0;
    public SimpleVector location;
    public float ground;
    public Meteor(int mID, int sur1, int sur2, Context ctx){
        pathAngle = 0f;
        if(context==null){
            context = ctx;
        }
        frameCounter2 = 11;
        frameCounter = 0;
        initVel= 0.05f;
        angle = 2.09f;
        initX = (float)Math.cos(angle)*initVel;
        initY = (float)Math.sin(angle)*initVel;
        velX = initX;
        velY = initY - GRAVITY*pathAngle;
        //tf = true;
       // tf2 = false;
        //meteor_wt = new TexturedPlane(0.2f, 0.2f,context,R.drawable.flame_white);
        //mblue = new TexturedPlane(0.8f, 1.0f,context,R.drawable.flame_blue);
        meteor_front = new TexturedPlane(0.4f, 0.4f,context,mID);
        //meteor_surr1 = new TexturedPlane(0.3f, 0.6f,context, R.drawable.thruster_i);
        //meteor_surr2 = new TexturedPlane(0.3f, 0.6f,context, R.drawable.thruster_ii);
        meteor_front.setDefaultTrans(0f,0f,2f);
        //mblue.setDefaultTrans(0f,0.0f,1f);
        //meteor_wt.setDefaultTrans(0f,0.6f,1.2f);
        //meteor_surr1.setDefaultTrans(0f,0f,2f);
      //  meteor_surr2.setDefaultTrans(0f,0f,2f);
        scaling = 1f;
        //this.getExplosionFrames();
        isParticleMt = false;
        active = true;
        fireactive = false;
    }

    public Meteor(boolean particle, SimpleVector loc, float speed, float angle){
        /*numPart = 20;
        counter = 0;
        delay = 30;*/
        fireAlpha = 1f;
        isParticleMt = particle;
        location = loc;
        trX = loc.x;
        trY = loc.y;
        trZ = loc.z;
        m = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0.4f,0f,0f), Color.rgb(249, 50, 10), 0, 10.0f, CustomParticles.LIGHT_BLEND, 20.0f, 800, 0.0f,0f);
        m.setBlendType(Particle.VN_BLEND);
        //overhead = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0.4f,0f,0f), Color.rgb(249, 250, 250), 0, 2.0f, CustomParticles.VN_BLEND, 6.0f, 300, 0.0f,0f);
        //pt_explosion = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0f,0.6f,0f), Color.rgb(222, 50, 10), 1500, 0.2f);
        pt_explosion = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0f,0.2f,0f), Color.rgb(222, 50, 10), R.drawable.q_particle_iii, 25.0f,CustomParticles.LIGHT_BLEND,20f, 1000, 0.1f, 2);
       // pt_explosion.setBlendType(Particle.LIGHT_BLEND);
        initVel= speed;
        angle = angle;
        initX = (float)Math.cos(angle)*initVel;
        initY = (float)Math.sin(angle)*initVel;
        velX = initX;
        velY = initY - GRAVITY*pathAngle;
        pathAngle = 0;
        active = true;
        fireactive = false;
    }

    public Meteor(boolean particle, SimpleVector loc, float speed, float angle, float pt){
        /*numPart = 20;
        counter = 0;
        delay = 30;*/
        overhead = null;
        fireAlpha = 1f;
        isParticleMt = particle;
        location = loc;
        trX = loc.x;
        trY = loc.y;
        trZ = loc.z;
        m = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0.4f,0f,0f), Color.rgb(249, 50, 10), 0, pt, CustomParticles.VN_BLEND, 5.0f, 800, 0.0f,0f);
        m.setBlendType(Particle.VN_BLEND);
        //pt_explosion = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0f,0.6f,0f), Color.rgb(222, 50, 10), 1500, 0.2f);
        pt_explosion = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0f,0.2f,0f), Color.rgb(222, 50, 10), R.drawable.q_particle_i, 15f,CustomParticles.LIGHT_BLEND,30f, 200, 0.05f, 2);
        // pt_explosion.setBlendType(Particle.LIGHT_BLEND);
        initVel= speed;
        angle = angle;
        initX = (float)Math.cos(angle)*initVel;
        initY = (float)Math.sin(angle)*initVel;
        velX = initX;
        velY = initY - GRAVITY*pathAngle;
        pathAngle = 0;
        active = true;
        fireactive = false;
    }

    public void onDrawFrame(float[] mMVPMatrix){
        if(!isParticleMt) {
            if (meteor_front.getTransformY() > SpaceGameRenderer.SURFACE) {
                pathAngle += 0.1f;
                //  float tBase = 1.0f + (float) Math.cos(pathAngle) * meteor_front.getDefaultY();
                // float tPerp = (float) Math.sin(pathAngle) * meteor_front.getDefaultY();
                //  float tPerp = -(float)(0.5)*0.00098f*(pathAngle*pathAngle);
                //  float tBase = (float)(0.0098);//*(pathAngle);
                //meteor_front.updateTransform(tBase, 0f,0f);
                velY = initY - GRAVITY * pathAngle;
                meteor_front.updateTransform(velX, velY, 0f);
                // meteor_surr1.changeTransform(tBase,tPerp,2f);
                // meteor_surr2.changeTransform(tBase,tPerp,2f);
                // mblue.draw(mMVPMatrix);
                //  meteor_wt.draw(mMVPMatrix);
        /*if(frameCounter<targetFrames){
            if(tf) {
                //scaling+=0.001f;
                //meteor_surr1.scale(0.01f, 0.01f);
                meteor_surr1.draw(mMVPMatrix);
            }else if(!tf) {
                meteor_surr2.draw(mMVPMatrix);
            }
            frameCounter++;
        }else{
            frameCounter=0;
            tf = !tf;
            //..scaling = 1f;
          //  meteor_surr1.setScaleY(1.0f);
          //  meteor_surr1.setScaleX(1.0f);
            *//*if(tf2){
                meteor_surr1.scale(1.5f,1.5f);
            }else{
                meteor_surr1.scale(1f,1f);
            }*//*
        }*/

            } else {
                mt_explosion.changeTransform(meteor_front.transformX, meteor_front.transformY + 0.2f, 2f);
                mt_explosion.startAnimation();
                meteor_front.setDefaultTrans(0f, 0f, 2f);
                pathAngle = 0;
            }
            meteor_front.draw(mMVPMatrix);
            mt_explosion.onDrawFrame(mMVPMatrix);

        }else{
            this.onUpdateFrame();
            pt_explosion.onDrawFrame(mMVPMatrix);
            m.onDrawFrame(mMVPMatrix);
        }
    }

    public void onUpdateFrame(){
        if(isParticleMt && (active || fireactive || platformCollision)){

            if(SpaceGameRenderer.cameraY>1.0f && trY<SpaceGameRenderer.cameraY-1.2f){
                active = false;
                fireactive = false;
                int rand = (int)(Math.random()*100);
                if(rand%2==0) {
                    this.reactivateWithNewLoc(-1.9f, SpaceGameRenderer.cameraY + 0.5f);
                }else{
                    this.reactivateWithNewLoc(1.9f, SpaceGameRenderer.cameraY + 0.1f);
                }
            }
            if(this.location.x >= SpaceGameRenderer.cameraX + GLRenderer.RATIO && this.trX<SpaceGameRenderer.cameraX-GLRenderer.RATIO){
                this.active = false;
                this.fireactive = false;
                int rand = (int)(Math.random()*100);
                if(rand%2==0) {
                    this.reactivateWithNewLoc(-1.9f, SpaceGameRenderer.cameraY + 0.5f);
                }else{
                    this.reactivateWithNewLoc(1.9f, SpaceGameRenderer.cameraY + 0.1f);
                }
            }
            if(this.location.x <= SpaceGameRenderer.cameraX-GLRenderer.RATIO && this.trX>SpaceGameRenderer.cameraX+GLRenderer.RATIO){
                this.active = false;
                this.fireactive = false;
                int rand = (int)(Math.random()*100);
                if(rand%2==0) {
                    this.reactivateWithNewLoc(-1.9f, SpaceGameRenderer.cameraY + 0.5f);
                }else{
                    this.reactivateWithNewLoc(1.9f, SpaceGameRenderer.cameraY + 0.1f);
                }
            }
            ground = -0.6f;//SpaceGameRenderer.cameraY-1.0f;
            //if(SpaceGameRenderer.cameraY<0.5f) {
            if (trX > -GLRenderer.RATIO && trX < GLRenderer.RATIO) {
                int locX = SpaceGameRenderer.groundar2[1].length / 2 + (int) ((trX / GLRenderer.RATIO) * SpaceGameRenderer.groundar2[0].length / 2);
                ground = PlatformsContainer.base.getDefaultY() + PlatformsContainer.base.getHeight() / 2 - SpaceGameRenderer.groundar2[1][locX];
            }
          //  }

            if (trY > ground && active) {
                pathAngle += 0.05f;
                velY = initY - GRAVITY * pathAngle;

                trX += velX / 30;
                trY += velY / 30;

                m.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY / 30, 0f), 4);
                float var = (float)Math.random()*0.05f;
                m.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, velY/30 +var, 0f), 1);
                m.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY/30 -var, 0f), 1);
                    /*if(overhead!=null) {
                        overhead.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY / 30 - 0.15f, 0f), 3);
                        overhead.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY / 30 + 0.15f, 0f), 3);
                    }*/
                //var = (float)Math.random()*0.05f;
                //pt_explosion.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY/30 -var, 0f), 1);

            } else {
                active = false;
                if (fireCounter < fireFrames) {
                    fireactive = true;
                    pt_explosion.addParticles(new SimpleVector(trX, trY - 0.06f, trZ), 10);

                    fireCounter++;
                } else {
                    fireactive = false;
                }
            }
        }
    }


    private void getExplosionFrames(){
        mt_explosion = new Animation(24,2,false);
        float len = 0.8f;
        float hi = 1.2f;
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_a));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_b));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_c));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_d));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_e));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_f));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_g));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_h));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_i));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_j));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_k));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_l));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_m));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_n));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_o));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_p));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_q));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_r));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_s));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_t));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_u));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_v));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_w));
        mt_explosion.addFrame(new TexturedPlane(len,hi,context,R.drawable.explosion_x));
    }

    public boolean reactivate(){
        if(!active) { //&& !fireActive
            fireCounter = 0;
            trX = this.location.x;
            trY = (float) Math.random();
            pathAngle = 0f;
            active = true;
            return true;
        }
        return false;
    }

    public boolean reactivateWithNewLoc(float x, float y){
        if(!active) { //&& !fireActive
            fireCounter = 0;
            trX = x;
            trY = y+(float) Math.random();
            this.location.x = trX;
            this.location.y = trY;
            pathAngle = 0f;
            active = true;
            fireactive = false;
            return true;
        }
        return false;
    }

    public void reactivateWCustomCoords(float x, float y){
        if(!active) { //&& !fireActive
            fireCounter = 0;
            trX = x;
            trY = y;
            pathAngle = 0f;
            active = true;
        }
    }
}
