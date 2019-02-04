package com.akashapps.shoottospace;

public class Animation {
    public TexturedPlane[] frames;
    private int delay;
    private int counter;
    private int size, add;
    public int currFrame, currFrameRev;
    public boolean active, activeRev, cont, scalable;
    private float scalePC;
    public Animation(int size, int d, boolean continuous){
        this.size = size;
        frames = new TexturedPlane[size];
        delay = d;
        currFrame = 0;
        counter = 0;
        active = false;
        activeRev = false;
        add = 0;
        cont = continuous;
        scalable = false;
    }

    public Animation(int size, int d, boolean scalable, float scalePS){
        this.size = size;
        frames = new TexturedPlane[size];
        delay = d;
        currFrame = 0;
        counter = 0;
        active = false;
        activeRev = false;
        add = 0;
        this.scalable = scalable;
        scalePC = scalePS;
    }


    public boolean addFrame(TexturedPlane tp){
        if(add<size){
            frames[add] = tp;
            add++;
            return true;
        }else{
            return false;
        }
    }

    public void onDrawFrame(float[] mMVPMatrix){
        if(active) {
            if (counter < delay) {
                frames[currFrame].draw(mMVPMatrix);
                if(scalable) {
                    if (currFrame < size / 2) {
                        this.scale(scalePC, scalePC);
                    } else {
                        this.scale(-scalePC, -scalePC);
                    }
                }
                counter++;
            } else {
                if(!cont) {
                    if (currFrame < size - 1) {
                        counter = 0;
                        currFrame++;
                    } else {
                        active = false;
                    }
                }else if(cont){
                    if (currFrame < size - 1) {
                        counter = 0;
                        currFrame++;
                    } else {
                        this.startAnimation();
                    }
                }
            }
        }
    }

    public void onDrawFrameRev(float[] mMVPMatrix){
        if(activeRev) {
            if (counter < delay) {
                frames[currFrameRev].draw(mMVPMatrix);
                counter++;
            } else {
                if (currFrameRev >0) {
                    counter = 0;
                    currFrameRev--;
                } else {
                    activeRev = false;
                }
            }
        }
    }

    public void startAnimationRev(){
        if(!activeRev) {
            currFrameRev = size;
            activeRev = true;

        }
    }

    public void startAnimation(){
        if(!cont && !active) {
            currFrame = 0;
            active = true;
            this.resetScale();
            counter = 0;
        }else if(cont){
            currFrame = 0;
            active = true;
            //this.resetScale();
            counter = 0;
        }
    }

    public void changeTransform(float x, float y, float z){
        for(int i=0;i<size;i++){
            frames[i].changeTransform(x,y,z);
        }
    }

    public void updateTransform(float x, float y, float z){
        for(int i=0;i<size;i++){
            frames[i].updateTransform(x,y,z);
        }
    }

    public float getTransformY(){return frames[0].transformY;}
    public float getTransformX(){return frames[0].transformX;}
    public void drawStillFrame(float[] mMVPMatrix){
        frames[currFrame].draw(mMVPMatrix);
    }

    public void scale(float x, float y){
        for(int i=0;i<size;i++){
            frames[i].scale(x,y);
        }
    }

    public void resetScale(){
        for(int i=0;i<size;i++){
            frames[i].scaleX = 1f;
            frames[i].scaleY = 1f;
        }
    }
}
