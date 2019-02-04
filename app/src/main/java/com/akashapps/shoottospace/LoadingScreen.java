package com.akashapps.shoottospace;

import android.content.Context;
import android.opengl.Matrix;

import com.akashapps.shoottospace.Utilities.Utilities;

public class LoadingScreen {
    private TexturedPlane background, head, cloud1, cloud2, planet;
    private ClickableIcon playIcon, playText;
    private boolean playTapped;
    private final int bounceTarget = 30;
    private int bounceTarget2 = 60;
    private int counter = 0;
    private int counter2 = 0;
    private int headCounter = 0;
    private int headCounter2 = 0;
    private boolean sup = true;
    private float f;

    public LoadingScreen(Context context){
        //float sw = Utilities.getScreenWidthPixels()/Utilities.getScreenWidthPixels() *2;
        float h = 2.0f;
        background = new TexturedPlane(1.88f*h,h,context, R.drawable.rickndm_bgc);
        head = new TexturedPlane(0.8f,0.8f,context,R.drawable.head);
        cloud1 = new TexturedPlane(0.6f,0.4f,context,R.drawable.cloud_i);
        cloud2 = new TexturedPlane(0.8f,0.4f,context,R.drawable.cloud_ii);
        planet = new TexturedPlane(0.5f,0.5f,context,R.drawable.planet_i);
        planet.setDefaultTrans(1.0f,0.8f,1f);
        cloud1.setDefaultTrans(-0.4f,0.3f,1f);
        cloud2.setDefaultTrans(-0.6f,0.6f,1f);
        head.setDefaultTrans(1.0f,0.4f,2.0f);
        playIcon = new ClickableIcon(R.drawable.play_ic_r,0.5f,0.5f,context);
        playIcon.setDefaultTrans(0f,0f,2f);
        playText = new ClickableIcon(R.drawable.play_t,0.6f,0.2f,context);
        playText.setDefaultTrans(0f,-0.7f,2f);
        playTapped = false;
    }

    public void onDrawFrame(float[] mMVPMatrix){
        background.draw(mMVPMatrix);
        planet.draw(mMVPMatrix);
        planet.rotateZ(0.1f);
        cloudsAnim(mMVPMatrix);
        headAnim(mMVPMatrix);
        playIconAnim(mMVPMatrix);
        playText.draw(mMVPMatrix);
        if(GLRenderer.DEV_MODE)
            GLRenderer.drawText("DEV_MODE_ON", new SimpleVector(0.0f,0.8f,2f), mMVPMatrix);

    }

    public void cloudsAnim(float[] mMVPMatirx){
        if(cloud1.getTransformX()+cloud1.getLength()/2>-GLRenderer.RATIO){
            cloud1.updateTransform(-0.001f,0f,0f);
        }else{
            cloud1.changeTransform(GLRenderer.RATIO+cloud1.getLength(),0.3f,0f);
        }
        if(cloud2.getTransformX()+cloud2.getLength()/2>-GLRenderer.RATIO){
            cloud2.updateTransform(-0.0005f,0f,0f);
        }else{
            cloud2.changeTransform(GLRenderer.RATIO+cloud2.getLength(),0.6f,0f);
        }
        cloud1.draw(mMVPMatirx);
        cloud2.draw(mMVPMatirx);
    }

    private void headAnim(float[] mMVPMatrix){
        if(headCounter<bounceTarget2) {
            float temp = -0.2f/(headCounter+bounceTarget2);
            head.updateTransform(0f, temp, 0f);
            headCounter++;
        }else if(headCounter2 <bounceTarget2){
           /* if(headCounter2==0){
                f = (float) Math.random() * (float) 0.001;
            }*/
            float temp = 0.2f/(headCounter2+bounceTarget2);
            head.updateTransform(0f, temp, 0f);
            headCounter2++;
        }else{
            headCounter = 0;
            headCounter2=0;
            bounceTarget2 = (int)(Math.random()*100);
        }
        head.draw(mMVPMatrix);
    }

    private void playIconAnim(float[] mMVPMatrix){
        playIcon.rotateZ(1f);
        if(!playTapped) {
            if (counter <= bounceTarget) {
                playIcon.scale(0.03f, 0.03f);
                counter++;
            } else if (counter2 <= bounceTarget) {
                playIcon.scale(-0.03f, -0.03f);
                counter2++;
            } else {
                counter = 0;
                counter2 = 0;
            }
        }
        playIcon.draw(mMVPMatrix);
    }

    public void onTouchDown(float x, float y){
        if(playIcon.isClicked(x,y)){
            playTapped=true;
        }else if(head.isClicked(x,y)){
            if(!GLRenderer.DEV_MODE) GLRenderer.DEV_MODE = true;
            else if(GLRenderer.DEV_MODE) GLRenderer.DEV_MODE = false;
        }
    }

    public boolean isPlayTapped(){return this.playTapped;}

    public void free(){
        this.planet = null;
        this.cloud1 = null;
        this.playIcon.free();
        this.cloud2 = null;
        this.head = null;
        this.background = null;
        this.playText = null;
    }
}
