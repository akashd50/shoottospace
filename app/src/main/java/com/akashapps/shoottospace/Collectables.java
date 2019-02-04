package com.akashapps.shoottospace;

import android.graphics.Color;
import static com.akashapps.shoottospace.SpaceGameRenderer.context;

public class Collectables {
    protected TexturedPlane[] collectables;
    protected Animation collectable;
    private int add, size;
    protected int score;
    private ParticleSystem glow;
    public Collectables(int size){
        this.size = size;
        collectables = new TexturedPlane[size];
        collectable = new Animation(8,10,true,0.5f);
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_i));
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_ii));
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_iii));
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_iv));
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_v));
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_vi));
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_vii));
        collectable.addFrame(new TexturedPlane(0.05f,0.05f,context,R.drawable.portal_anim_viii));
        for(int i=0;i<size;i++){
            float rx = (float)Math.random();
            if(i%2==0) rx = -rx*GLRenderer.RATIO;
            float ry = (float)Math.random()*10;
            collectables[i] = new TexturedPlane(0.15f,0.2f, context,R.drawable.collectable_i);
            collectables[i].setDefaultTrans(rx,ry,2f);
        }
        //glow = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0.1f,0.1f,0f), Color.rgb(12, 178, 7), 300, 0.0f);
        glow = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0.1f,0.1f,0f), Color.rgb(12, 178, 7),0,5.0f,CustomParticles.VN_BLEND, 5f, 300, 0f,0f);
        //glow.setBlendType(Particle.LIGHT_BLEND);
        score = 0;
        //this.size = size;
    }

    public boolean addCollectable(TexturedPlane tp){
        if(add<size){
            collectables[add] = tp;
            add++;
            return true;
        }else{
            return false;
        }
    }

    public void onDrawFrame(float[] mMVPMatrix, TexturedPlane collisionTex){
        float x = 999;
        float y =999;
        for(int i=0;i<size;i++){
            if(collectables[i].active) {
                glow.onDrawFrame(mMVPMatrix);
                collectables[i].draw(mMVPMatrix);
                if(collectables[i].transformY <y) {
                    x = collectables[i].transformX;
                    y = collectables[i].transformY;
                }
                if(collisionTex!=null){
                    if(collisionTex.onTriggerCollisionPoint(collectables[i].transformX, collectables[i].transformY)){
                        collectable.changeTransform(collectables[i].transformX, collectables[i].transformY, 1f);
                        collectable.startAnimation();
                        collectables[i].active = false;
                        score++;

                    }
                }
            }

        }
        glow.addParticlesPoint(new SimpleVector(x,y,1f), 4);

        collectable.onDrawFrame(mMVPMatrix);
    }

    public void onDrawFrame(float[] mMVPMatrix, Spaceship ship){
        float x = 999;
        float y =999;
        TexturedPlane collisionTex = ship.spaceship;
        for(int i=0;i<size;i++){
            if(collectables[i].active) {
                glow.onDrawFrame(mMVPMatrix);
                collectables[i].draw(mMVPMatrix);
                if(collectables[i].transformY <y) {
                    x = collectables[i].transformX;
                    y = collectables[i].transformY;
                }
                if(collisionTex!=null){
                    if(collisionTex.onTriggerCollisionPoint(collectables[i].transformX, collectables[i].transformY)){
                        collectable.changeTransform(collectables[i].transformX, collectables[i].transformY, 1f);
                        collectable.startAnimation();
                        collectables[i].active = false;
                        score++;
                        ship.fuelLevel+=10;
                    }
                }
            }

        }
        glow.addParticlesPoint(new SimpleVector(x,y,1f), 4);

        collectable.onDrawFrame(mMVPMatrix);
    }

    private void collisionChecking(){

    }
}
