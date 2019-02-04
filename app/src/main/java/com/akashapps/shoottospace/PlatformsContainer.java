package com.akashapps.shoottospace;

import android.graphics.Color;

import static com.akashapps.shoottospace.SpaceGameRenderer.context;
import android.graphics.Color;
import android.opengl.Matrix;
import android.util.Log;

public class PlatformsContainer {

    public TexturedPlane[] platforms;
    public static TexturedPlane base;
    private int numPlatforms;
    private int counter;

    public PlatformsContainer(){
        numPlatforms = 10;
        counter = 0;
        platforms = new TexturedPlane[numPlatforms];
        float[] basell = {0.0f,0.5f,0f};
        base = new TexturedPlane(GLRenderer.RATIO*2f, 0.4f,context,R.drawable.base_r, SpaceGameRenderer.reddishTint, basell);
        //base.scale(3.0f,3.0f);
        base.setDefaultTrans(0f,-0.8f,1f);
        TexturedPlane temp = new TexturedPlane(1.0f, 0.3f, context, R.drawable.platform_ii);
        temp.getSquaredArray(50, 5, R.drawable.platform_ii, 1.0f, 0.3f);
        temp.setDefaultTrans(0f, 0f, 2f);
        platforms[counter++] = temp;

        for(int i=1;i<9;i++) {
            TexturedPlane temp1 = new TexturedPlane(1.0f, 0.3f, context, R.drawable.platform_ii);
            temp1.collisionCoords = temp.collisionCoords;
            float x = (float)Math.random()*4 - 2.0f;
            float y = (float)Math.random()*15 +1f;
            temp1.setDefaultTrans(x, y, 2f);
            platforms[counter++] = temp1;
        }
        //float[][] t = temp.collisionCoords;
       // Log.v("Collision Length:",t[0].length+"");
        /*for(int i=0;i<t[0].length;i++){
            Log.v("Collision:","X: "+t[0][i]+" Y1: "+t[1][i]+" Y2: "+t[2][i]);
        }*/

    }

    public void onDrawFrameBG(float[] mMVPMatrix){

    }

    public void onDrawFrameFG(float[] mMVPMatrix){
      for(int i=0;i<counter;i++){
          platforms[i].draw(mMVPMatrix);
      }
      base.draw(mMVPMatrix);
    }

    public void onUpdateFrame(float scale){
        final float scale2 = scale;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<platforms.length;i++){
                    if(platforms[i].transformX>0){
                        platforms[i].transformX = platforms[i].defTransX - (float)(1.0-scale2);
                    }else if(platforms[i].transformX<0){
                        platforms[i].transformX = platforms[i].defTransY + (float)(1.0-scale2);
                    }
                }
            }
        }).start();

    }

    public boolean onTriggerCollision(Spaceship s){
        final TexturedPlane tp = s.spaceship;
        final Spaceship s1 = s;
        /*if(SimpleVector.distanceXYPoints(tp.transformX, tp.transformY, base.transformX, base.transformY)<Spaceship.CLOSEST_GROUND_DIST) {
            if (base.onTriggerCollision(tp)) {
                Spaceship.GROUND = platforms[0].transformY+0.15f;
            }
        }*/
       new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<counter;i++){
                   //if(SimpleVector.distanceXYPoints(tp.transformX, tp.transformY, platforms[i].transformX, platforms[i].transformY)<Spaceship.CLOSEST_GROUND_DIST) {

                    if(!s1.FLAME_ON && platforms[i].transformY <= tp.transformY && SimpleVector.distanceXY(
                            new SimpleVector(tp.transformX, tp.transformY,0f),
                            new SimpleVector(platforms[i].transformX, platforms[i].transformY,0f))< platforms[i].l) {
                        if ( platforms[i].onTriggerCollision(tp)) {
                            // Spaceship.GROUND = platforms[i].transformY+0.15f;
                            s1.PLATFORM_COLLISSION = true;
                            s1.velY = 0f;
                            //break;
                        } else {
                            s1.PLATFORM_COLLISSION = false;
                        }
                    }else{
                        if(s1.FLAME_ON) s1.PLATFORM_COLLISSION = false;
                    }
                   // }
               }
            }
        }).start();
        return false;
    }

    public boolean onTriggerCollisionPoint(Meteor m){
        final float xx = m.trX;
        final float yy = m.trY;
        final Meteor mm = m;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<counter;i++){
                    if(platforms[i].transformY <= yy && SimpleVector.distanceXY(new SimpleVector(xx, yy,0f),
                            new SimpleVector(platforms[i].transformX, platforms[i].transformY,0f))< platforms[i].l) {
                        //if(SimpleVector.distanceXYPoints(tp.transformX, tp.transformY, platforms[i].transformX, platforms[i].transformY)<Spaceship.CLOSEST_GROUND_DIST) {
                        if (platforms[i].onTriggerCollisionPoint(xx, yy)) {
                            mm.active = false;
                            mm.platformCollision = true;
                        }
                    }
                }
            }
        }).start();
        return false;
    }

    public void scale(float x, float y){
        for(int i=0;i<counter;i++){
            platforms[i].scale(x,y);
        }
        base.scale(x,y);
    }

    public void setScale(float x, float y){
        for(int i=0;i<counter;i++){
            platforms[i].scaleX = x;
            platforms[i].scaleY = y;
        }
        base.scaleX = x;
        base.scaleY = y;
    }

    public void updateTransformFG(){

    }
}
