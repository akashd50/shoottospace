package com.akashapps.shoottospace;

import android.content.Context;
import android.content.res.Resources;

public class SpecializedRect {


    private int scrWidth,scrHeight;

    private float left,top, right, bottom;
    private float transformY,transformX;

    private boolean active;

    private float color[];
    private float color2[];

    private SpecializedTriangle t1;//= new Triangle(v1,c);
    private SpecializedTriangle t2;
    private float ar1[],ar2[];
    private Context context;

    private float cx,cy,cz,l,w;

    public SpecializedRect(float cx,float cy, float cz,float l, float w,float[] c1,float[] c2, Context ctx, String type) {

        //sqCoords = vert;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.l = l;
        this.w = w;

        transformY = 0f;
        transformX = 0f;
        active = false;

        if(type.compareTo("Z")==0){
            setUpParmsReg(cx,cy,cz,l,w,c1,c2);
        }else if(type.compareTo("X")==0) {
            setUpParmsFX(cx, cy, cz, l, w, c1, c2);
        }else if(type.compareTo("Y")==0) {
            setUpParmsFY(cx, cy, cz, l, w, c1, c2);
        }

        scrWidth = getScreenWidth();
        scrHeight = getScreenHeight();
        context = ctx;
        convertPointSystem();
    }

    private void setUpParmsReg(float cx,float cy, float cz,float l, float w,float[] c1,float[] c2){
        float v1[] = {cx- (l/2),cy+ (w/2),cz,1.0f,
                cx-(l/2),cy-(w/2),cz,1.0f,
                cx+(l/2),cy-(w/2),cz,1.0f};

        float v2[] = {cx+(l/2),cy-(w/2),cz,1.0f,
                cx+(l/2),cy+(w/2),cz,1.0f,
                cx-(l/2),cy+(w/2),cz,1.0f};

        ar1 = v1;
        ar2 = v2;

        t1 = new SpecializedTriangle(v1,c1);
        t2 = new SpecializedTriangle(v2,c2);

    }

    private void setUpParmsFX(float cx,float cy, float cz,float l, float w,float[] c1,float[] c2){
        float v1[] = {cx- (l/2),cy,cz-w/2,1.0f,
                cx-(l/2),cy,cz+w/2,1.0f,
                cx+(l/2),cy,cz+w/2,1.0f};

        float v2[] = {cx+(l/2),cy,cz+w/2,1.0f,
                cx+(l/2),cy,cz-w/2,1.0f,
                cx-(l/2),cy,cz-w/2,1.0f};

        ar1 = v1;
        ar2 = v2;

        t1 = new SpecializedTriangle(v1,c1);
        t2 = new SpecializedTriangle(v2,c2);
    }

    private void setUpParmsFY(float cx,float cy, float cz,float l, float w,float[] c1,float[] c2){
        float v1[] = {cx,cy+w/2,cz+l/2,1.0f,
                cx,cy-w/2,cz+l/2,1.0f,
                cx,cy-w/2,cz-l/2,1.0f};

        float v2[] = {cx,cy-w/2,cz-l/2,1.0f,
                cx,cy+w/2,cz-l/2,1.0f,
                cx,cy+w/2,cz+l/2,1.0f};

        ar1 = v1;
        ar2 = v2;

        t1 = new SpecializedTriangle(v1,c1);
        t2 = new SpecializedTriangle(v2,c2);
    }

    public void draw(float[] mvpMatrix) {
        t1.draw(mvpMatrix);
        t2.draw(mvpMatrix);
    }

    public void convertPointSystem(){
        this.left = (scrWidth/2 + ar1[0]*scrWidth/2) + (this.transformX*(scrWidth/2));
        this.top = (float) (scrHeight/2-(ar1[1]*scrHeight/2)/1.7);
        this.right = (scrWidth/2 + ar2[0]*scrWidth/2) + (this.transformX*(scrWidth/2));
        // this.bottom = (int) (scrHeight/2- ar2[1]*scrHeight/2);
        this.bottom = (float) (scrHeight/2-(ar2[1]*scrHeight/2)/1.7);
    }

    public boolean isClicked(float tx, float ty){
        /*Toast m = Toast.makeText(context,"Left: "+this.left+" Right: "+this.right+
                " Top: "+this.top+" Bottom: "+this.bottom,Toast.LENGTH_SHORT);
        m.show();*/
        //Toast.makeText(context,"LEFT: "+left +" Right: "+right,Toast.LENGTH_SHORT).show();
        //if(tx - (this.transformX*(scrWidth/2)) > left && tx -(this.transformX*(scrWidth/2)) < right && ty < bottom && ty > top) {
        if(tx > left && tx < right && ty < bottom && ty > top) {
            return true;
        }else return false;
    }

    public static int getScreenWidth () {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight () {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void updateTrasnformY(float vy){
        transformY+=vy;
    }
    public float getTransformY(){return this.transformY;}

    public void updateTrasnformX(float vx){
        transformX+=vx;
        convertPointSystem();
    }
    public void changeTrasnformX(float vx){
        transformX =vx;
        convertPointSystem();
    }
    public float getTransformX(){return this.transformX;}

    public void deactivate(){
        active = false;
        transformY = 0;
        transformX = 0;
        //transformY = 0f;
    }
    public void activate(){active = true;}

    public boolean isActive(){return this.active;}

    public float getCX(){return this.cx;}
    public float getCY(){return this.cy;}
    public float getCZ(){return this.cz;}
}
