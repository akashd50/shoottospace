package com.akashapps.shoottospace;

import android.content.Context;
import android.graphics.Color;
import javax.microedition.khronos.opengles.GL;

public class EMeteor {
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
    private ParticleSystem m, pt_explosion;
    public float trX, trY, trZ;
    private static int fireFrames = 600;
    private int numPart, delay, counter;
    private int fireCounter = 0;
    private SimpleVector location;
    public float ground;

    public EMeteor(SimpleVector loc, float speed, float angle){
        /*numPart = 20;
        counter = 0;
        delay = 30;*/
        fireAlpha = 1f;
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

    public EMeteor(SimpleVector loc, float speed, float angle, float pt){
        fireAlpha = 1f;
        location = loc;
        trX = loc.x;
        trY = loc.y;
        trZ = loc.z;
        m = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0.4f,0f,0f), Color.rgb(249, 50, 200), 0, pt, CustomParticles.VN_BLEND, 5.0f, 800, 0.0f,0f);
        m.setBlendType(Particle.VN_BLEND);
        pt_explosion = new ParticleSystem(new SimpleVector(trX,trY,trZ), new SimpleVector(0f,0.2f,0f), Color.rgb(222, 50, 200), R.drawable.q_particle_iii, 15f,CustomParticles.LIGHT_BLEND,30f, 200, 0.05f, 2);
        initVel= speed;
        this.angle = angle;
        initX = (float)Math.cos(angle)*initVel;
        initY = (float)Math.sin(angle)*initVel;
        velX = initX;
        velY = initY - GRAVITY*pathAngle;
        pathAngle = 0;
        active = true;
        fireactive = false;
        platformCollision = false;
    }

    public void onDrawFrame(float[] mMVPMatrix){
            this.onUpdateFrame();
            //pt_explosion.onDrawFrame(mMVPMatrix);
            m.onDrawFrame(mMVPMatrix);
            pt_explosion.onDrawFrame(mMVPMatrix);
    }

    public void onUpdateFrame(){
        if((active || fireactive || platformCollision)){

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
            ground = -0.6f;//SpaceGameRenderer.cameraY-1.0f;
            //if(SpaceGameRenderer.cameraY<0.5f) {
            if (trX > -GLRenderer.RATIO && trX < GLRenderer.RATIO) {
                int locX = SpaceGameRenderer.groundar2[1].length / 2 + (int) ((trX / GLRenderer.RATIO) * SpaceGameRenderer.groundar2[0].length / 2);
                ground = PlatformsContainer.base.getDefaultY() + PlatformsContainer.base.getHeight() / 2 - SpaceGameRenderer.groundar2[1][locX];
            }

            if (trY > ground && active) {
                pathAngle += 0.05f;
                velY = initY - GRAVITY * pathAngle;

                trX += velX / 30;
                trY += velY / 30;

                m.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY / 30, 0f), 4);
                float var = (float)Math.random()*0.05f;
                m.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, velY/30 +var, 0f), 1);
                m.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY/30 -var, 0f), 1);

                pt_explosion.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY/30 -var, 0f), 1);
                    /*if(overhead!=null) {
                        overhead.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY / 30 - 0.15f, 0f), 3);
                        overhead.addParticlesAndDirection(new SimpleVector(trX, trY, trZ), new SimpleVector(-velX / 30, -velY / 30 + 0.15f, 0f), 3);
                    }*/

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

