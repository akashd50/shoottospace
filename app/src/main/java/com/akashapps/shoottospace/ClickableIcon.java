package com.akashapps.shoottospace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.lang.UCharacter;
import android.opengl.Matrix;

import com.akashapps.shoottospace.minigame1.LiftOffActivity;

public class ClickableIcon {
    //Bitmap resourse;
    float lenght;
    float height;
    TexturedPlane icon, iconClicked;
   // Square background;
    private float transformY,transformX,transformZ;
    private float defTransX,defTransY, defTransZ;
    private Class specialActivity;
    private Window activity;
    private Context context;
    public boolean isClicked;

    public ClickableIcon(int resID, float len, float hi, Context c){
        lenght = len;
        height = hi;
        context = c;
        //resourse = BitmapFactory.decodeResource(c.getResources(),resID);
        Bitmap b = null;
        icon = new TexturedPlane(0f,0f,0f,lenght,height,c,resID,b);
        float[] col = {0f,0f,0f,1f};
        // background = new Square(lenght,height,col,c);
        isClicked = false;
    }
    public ClickableIcon(int resID, int resID2, float len, float hi, Context c){
        lenght = len;
        height = hi;
        context = c;
        //resourse = BitmapFactory.decodeResource(c.getResources(),resID);
        Bitmap b = null;
        icon = new TexturedPlane(lenght,height,c,resID);
        iconClicked = new TexturedPlane(lenght,height,c,resID2);
        float[] col = {0f,0f,0f,1f};
        isClicked = false;
        // background = new Square(lenght,height,col,c);
        // isClicked = false;
    }

    public void setActivity(Window w){
        this.activity = w;
    }

    public void setSpecialActivity(Class a){
        this.specialActivity = a;
    }

    public void draw(float[] mvpMatrix) {
        if(iconClicked!=null) {
            if (!isClicked) {
                icon.draw(mvpMatrix);
            } else {
                iconClicked.draw(mvpMatrix);
            }
        }else{
            icon.draw(mvpMatrix);
        }
    }

    public boolean isClicked(float tx, float ty){
        if(icon.isClicked(tx,ty)) {
            isClicked = true;
            if(this.activity!=null) {
                activity.launched();
            }
            return true;
        }else {
            isClicked = false;
            return false;
        }
    }

    public void onTouchUp(){
        isClicked = false;
    }

    public void setDefaultTrans(float x,float y,float z){
        icon.setDefaultTrans(x,y,z);
        if(iconClicked!=null){
            iconClicked.setDefaultTrans(x,y,z);
        }
       // background.setDefaultTrans(x,y,z);
    }
    public float getDefaultX(){return icon.getDefaultX();}
    public float getDefaultY(){return icon.getDefaultY();}
    public float getDefaultZ(){return icon.getDefaultZ();}
    public void changeTransform(float x,float y,float z){
        icon.changeTransform(x,y,z);
        //background.changeTransform(x,y,z);
    }

    public void rotateX(float angle){
        icon.rotateX(angle);
    }

    public void rotateY(float angle){
        icon.rotateY(angle);
    }

    public void rotateZ(float angle){
        icon.rotateZ(angle);
    }

    public Window getActivity(){
        return activity;
    }
    public float getLength(){return this.lenght;}
    public float getHeight(){return this.height;}
    public void scale(float x, float y){
        icon.scale(x,y);
    }
    public float getScaleX(){return icon.getScaleX();}
    public float getScaleY(){return icon.getScaleY();}

    public void free(){
        this.icon = null;
        this.context = null;
    }
}
