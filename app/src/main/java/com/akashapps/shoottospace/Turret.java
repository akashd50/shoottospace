package com.akashapps.shoottospace;

import android.graphics.Color;

public class Turret {
    private TexturedPlane turretStand, target;
    private ClickableIcon fire, fireGlow, iconToShow;
    public boolean active, bulletInProgress, newBulletAvailable;
    private Projectile bullet;
    private int timer, framesPerBullet = 300;

    public Turret(SimpleVector location){
        timer = 0;
        newBulletAvailable = true;
        turretStand  =new TexturedPlane(0.2f,0.3f,SpaceGameRenderer.context, R.drawable.crash);
        target = new TexturedPlane(0.2f,0.2f,SpaceGameRenderer.context, R.drawable.turret_target);

        turretStand.setDefaultTrans(1.0f,-0.6f,2f);
        target.setDefaultTrans(0f,0f,2f);
        active = false;
        bulletInProgress = false;

        fire = new ClickableIcon(R.drawable.fire_ic, 0.3f,0.3f, SpaceGameRenderer.context);
        fireGlow = new ClickableIcon(R.drawable.fire_ic_glow, 0.3f,0.3f, SpaceGameRenderer.context);
        fire.setDefaultTrans(1.5f,-0.2f, 2f);
        fireGlow.setDefaultTrans(1.5f,-0.2f, 2f);
        iconToShow = fire;
        bullet = new Projectile(new SimpleVector(turretStand.transformX, turretStand.transformY,2f),0.015f, R.drawable.rocket);
    }

    public void onDrawFrame(float[] mMVPMatrix){
        turretStand.draw(mMVPMatrix);
       // GLRenderer.drawText("A: "+angle,new SimpleVector(-1.5f,0.2f,2f),GLRenderer.mMVPMatrix);
        //fire.draw(mMVPMatrix);
        if(active) {
            target.draw(mMVPMatrix);
            bullet.onDrawFrame(mMVPMatrix);
        }
    }

    public void onDrawFrameStatic(float[] mMVPMatrix){
        if(newBulletAvailable) {
            fireGlow.draw(mMVPMatrix);
        }else{
            fire.draw(mMVPMatrix);
            timer++;
            if(timer>=framesPerBullet){
                newBulletAvailable = true;
                timer = 0;
            }
        }
    }

    public void onTouchInput(DPad gamepad){
        if(gamepad.isClicked()){
            target.updateTransform(gamepad.activeDpadX*0.05f, gamepad.activeDpadY*0.05f, 0f);
        }
    }

    public void onTouchDown(float x, float y){
        if(newBulletAvailable && fireGlow.isClicked(x,y)){
            bullet.shootProjectile(target.transformX, target.transformY);
            newBulletAvailable = false;
        }
    }

}
