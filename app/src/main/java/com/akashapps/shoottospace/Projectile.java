package com.akashapps.shoottospace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
public class Projectile {
    private ParticleSystem projectile, explosion;
    public boolean inProgress, explosionInProgress, angledProjectile;

    private float targetX, targetY, angle;
    private float velX, velY, pathAngle, trX, trY, initY, initVel, initX;
    private static int fireFrames = 5;
    private int fireCounter = 0;
    private static float GRAVITY = 0.001f;

    private float angleRatio;

    private SimpleVector location;
    //private TexturedPlane bitmap;
    public Projectile(SimpleVector location, float vel, int resID){
        this.location = location;
        inProgress = false;
        explosionInProgress = false;
        targetX = -1;
        targetY = -1;
        projectile = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0f,0f,0f), Color.rgb(50,90,200), 0, 30f,CustomParticles.VN_BLEND, 20f,1200, 0f,0f);
        explosion = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(1.0f,1.0f,0f), Color.rgb(225,90,40), R.drawable.q_particle_iii, 50f, CustomParticles.LIGHT_BLEND, 20f, 300, 0f, 0f);
        pathAngle = 0f; velX=0f; velY=0f; trX=0f;trY = 0f;
        initY = 0f; initY=0;
        initVel = vel;
        angledProjectile = false;
       /// bitmap = new TexturedPlane(0.05f,0.2f,SpaceGameRenderer.context,resID);
      //  bitmap.setDefaultTrans(location.x, location.y, location.z);
    }
    
    public Projectile(SimpleVector loc, float vel, float angle, int numParts, float ptSize, int color){
        this.location = loc;
        inProgress = false;
        explosionInProgress = false;
        targetX = -1;
        targetY = -1;
        projectile = new ParticleSystem(new SimpleVector(0f,0f,2f), new SimpleVector(0f,0f,0f), color, 0, ptSize,CustomParticles.VN_BLEND, 20f,numParts, 0f,0f);
        pathAngle = 0f; velX=0f; velY=0f; trX=0f;trY = 0f;
        this.angle = angle;
        initY = 0f; initY=0;
        initVel = vel;
        angledProjectile = true;
    }

    public void onDrawFrame(float[] mMVPMatrix){
      if(!angledProjectile) {
          if (inProgress) {
              float x = targetX - trX;
              float y = targetY - trY;
              float distance = (float) Math.sqrt(x * x + y * y);
              if (distance > 0.1f) {
                  pathAngle += 0.05f;
                  velY = initY - GRAVITY * pathAngle;
                /*trX +=-0.002;
                trY+=-0.001;*/
                  trX += velX;
                  trY += velY;
                  projectile.addParticlesAndDirection(new SimpleVector(trX, trY, 2f), new SimpleVector(-velX, -velY, 0f), 8);
                  float var = (float) Math.random() * 0.1f;
                  projectile.addParticlesAndDirection(new SimpleVector(trX, trY, 2f), new SimpleVector(-velX + var, velY + var, 0f), 1);
                  projectile.addParticlesAndDirection(new SimpleVector(trX, trY, 2f), new SimpleVector(-velX - var, -velY - var, 0f), 1);

                  projectile.onDrawFrame(mMVPMatrix);
                  // bitmap.draw(mMVPMatrix);
              } else {
                  if (fireCounter < fireFrames) {
                      explosion.addParticlesPoint(new SimpleVector(trX, trY, 2f), 60);
                      fireCounter++;
                  } else {
                      inProgress = false;
                      fireCounter = 0;
                  }

              }
          } else {
              targetX = -1;
              targetY = -1;
          }

          explosion.onDrawFrame(mMVPMatrix);
      }else{
          if (inProgress) {
              pathAngle += 0.05f;
              velY = initY - GRAVITY * pathAngle;
              trX += velX;
              trY += velY;
              projectile.addParticlesAndDirection(new SimpleVector(trX, trY, 2f), new SimpleVector(-velX, -velY, 0f), 8);
              float var = (float) Math.random() * 0.1f;
              projectile.addParticlesAndDirection(new SimpleVector(trX, trY, 2f), new SimpleVector(-velX + var, velY + var, 0f), 1);
              projectile.addParticlesAndDirection(new SimpleVector(trX, trY, 2f), new SimpleVector(-velX - var, -velY - var, 0f), 1);
              projectile.onDrawFrame(mMVPMatrix);
              if(trY<location.y){
                  inProgress = false;
              }
          }
      }
    }

    public void shootProjectileAngled(SimpleVector loc){
        inProgress = true;
        this.location = loc;
        trX = location.x;
        trY = location.y;
        float initVel = this.initVel;
        initX = (float)Math.cos(angle)*initVel;
        initY = (float)Math.sin(angle)*initVel;
        velX = initX;
        pathAngle = 0;
        velY = initY - GRAVITY*pathAngle;
    }

    public void shootProjectile(float x, float y){
        targetX = x;
        targetY = y;
        inProgress = true;
        trX = location.x;
        trY = location.y;

        float x2_x1 = 0f;
        if(targetX>location.x) {
            x2_x1 = (float) Math.sqrt((targetX - location.x) * (targetX - location.x));
        }else{
            x2_x1 = -(float) Math.sqrt((targetX - location.x) * (targetX - location.x));
        }
        float y2_y1 = (float)Math.sqrt((targetY-location.y)*(targetY-location.y));
        float hypo = (float)Math.sqrt((x2_x1*x2_x1) + (y2_y1*y2_y1));
        // angle = (float)(3.14 - Math.tan(perp/base));
        angle = (float)(Math.acos(x2_x1/hypo));

        //bitmap.setDefaultTrans(location.x-bitmap.getLength()/2, location.y+bitmap.getLength()/2,location.z);
       /* bitmap.setDefaultTrans(location.x, location.y,location.z);
        bitmap.rotateZ=0f;
        bitmap.rotateZ(-90);

        float degrees = (float)(angle*(180/Math.PI));
        bitmap.rotateZ(degrees);
        angleRatio = Math.abs(90-degrees)/x2_x1;*/

        float initVel = this.initVel;
        initX = (float)Math.cos(angle)*initVel;
        initY = (float)Math.sin(angle)*initVel;
        velX = initX;
        pathAngle = 0;
        velY = initY - GRAVITY*pathAngle;
    }

}
