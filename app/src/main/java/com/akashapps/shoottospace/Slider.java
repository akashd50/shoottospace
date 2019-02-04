package com.akashapps.shoottospace;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.akashapps.shoottospace.Utilities.Utilities;
public class Slider {
    public static final String HOR_SLIDER = "horizontal";
    public static final String VER_SLIDER = "vertical";

    private float currSpeed, length;

    private boolean isClicked;
    private String type;

    private Square sliderBack, sliderSwitch;
    private TexturedPlane icon, backLine;

    public Slider(SimpleVector center, String type, float len, Context context){
        isClicked = false;
        length = len;
        this.type = type;
        Bitmap bp = null;
        icon = new TexturedPlane(0f,0f,0f,0.2f,0.2f,context,R.mipmap.slider_iconn,bp);
        backLine = new TexturedPlane(0f,0f,0f,0.02f,len,context,R.mipmap.slider_back,bp);
        if(type.compareTo(HOR_SLIDER)==0) {
            float[] c = {1.0f,1.0f,1.0f,0.3f};
            sliderBack = new Square(len,0.02f,c,context);
            float[] c2 = {0.9f,0f,0f,0.0f};
            sliderSwitch = new Square(0.1f,0.1f,c2,context);
            sliderSwitch.setDefaultTrans(center.x,center.y,center.z);
            sliderBack.setDefaultTrans(center.x,center.y,center.z);
        }else if(type.compareTo(VER_SLIDER)==0){
            float[] c = {1.0f,1.0f,1.0f,1.0f};
            float[] c2 = {0.9f,0f,0f,0.8f};
            sliderBack = new Square(0.02f,len,c,context);
            sliderSwitch = new Square(0.2f,0.2f,c2,context);
            sliderSwitch.setDefaultTrans(center.x,center.y,center.z);
            sliderBack.setDefaultTrans(center.x,center.y,center.z);
            backLine.setDefaultTrans(center.x,center.y,center.z);
            icon.setDefaultTrans(center.x,center.y,center.z);
        }else{

        }
    }

    public void draw(float[] mMVPMatrix){
        icon.draw(mMVPMatrix);
        //sliderSwitch.draw(mMVPMatrix);
        //sliderBack.draw(mMVPMatrix);
        backLine.draw(mMVPMatrix);
    }

    public boolean onTouchDown(float x,float y){
        if(sliderSwitch.isClicked(x,y)){
            this.isClicked = true;
        }
        return this.isClicked;
    }

    public void onTouchMove(float x,float y){
        if(this.isClicked){
            float tempX = (x - Utilities.getScreenWidthPixels()/2) / (Utilities.getScreenWidthPixels()/2);
            //float tempY = (y-Utilities.SCREEN_HEIGHT/2)/ (Utilities./2);
            float tempY = (Utilities.getScreenHeightPixels()/2-y)*(2/Utilities.getScreenHeightPixels());
            if(this.type.compareTo(HOR_SLIDER)==0){
                if(x>sliderBack.getLeft() &&
                        x<sliderBack.getRight()){
                    sliderSwitch.changeTrasnformX(tempX);

                }
            }else if(this.type.compareTo(VER_SLIDER)==0){
                if(y>sliderBack.getTop() &&
                        y<sliderBack.getBottom()){
                    sliderSwitch.changeTrasnformY(tempY);
                    icon.changeTransform(icon.getDefaultX(),tempY,icon.getDefaultZ());
                }
            }
        }
    }

    public void onTouchUp(float x,float y){
        isClicked = false;
        sliderSwitch.changeTrasnformY(sliderSwitch.getDefaultY());
        icon.changeTransform(icon.getDefaultX(),icon.getDefaultY(),icon.getDefaultZ());
    }

    public float getLength(){ return this.length;}
    public String getType(){return this.type;}

    public float distanceFromOrigin(){
        float distance = -1;
        float temp = -1;
        if(type.compareTo(VER_SLIDER)==0){
            temp = sliderBack.getCenterY() - sliderSwitch.getCenterY();
            distance = 1.0f - (Utilities.getScreenHeightPixels() / 2 - temp) * (2 / Utilities.getScreenHeightPixels());
        }else{
            temp = sliderSwitch.getCenterX() - sliderBack.getLeft();
            distance = 1.0f-(temp - Utilities.getScreenWidthPixels()/2) / (Utilities.getScreenWidthPixels()/2);
        }
        return distance;
    }

    public void setSliderSpeed(float speed){currSpeed = speed;}
    public float getSliderSpeed(){return this.currSpeed;}

    public float getSCY(){return sliderSwitch.getCenterY();}
    public float getSCT(){return sliderSwitch.getTop();}
    public float getSCB(){return sliderSwitch.getBottom();}

}
