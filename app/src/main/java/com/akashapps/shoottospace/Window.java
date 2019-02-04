package com.akashapps.shoottospace;

import android.content.Context;

import java.util.logging.Logger;

public class Window {
    private String tag;
    private ClickableIcon[] icons;
    private ClickableIcon terminatingIcon;
    private int totalSize, numIcons;
    private TexturedPlane background;
    private boolean isOpened;

    public static String HOME_WINDOW = "hwindow";
    public static String MENU_WINDOW = "mwindow";
    public static String GENERAL_WINDOW = "gwindow";
    public static String FULL_SCREEN_ACTIVITY = "fsactivity";
    public static String L_LANE = "left";
    public static String R_LANE = "right";

    public static Window[] OPEN_WINDOWS = new Window[5];
    public static int NUM_OPEN_WIN = 0;
    public static float TOTAL_WINS=0f;
    private DPad dpad;
    private Slider slider;

    private float lyCounter,ryCounter, left, right, top, bottom;

    public Window(String t, int s, int bgID, float l, float h, Context context){
        tag = t;
        totalSize = s;
        numIcons =0;
        icons = new ClickableIcon[s];

        background = new TexturedPlane(l,h,context,bgID);
        //background.setDefaultTrans(0f,0f,TOTAL_WINS);
        terminatingIcon = null;
        lyCounter = background.getDefaultY()+h/2 - 0.2f;
        ryCounter = lyCounter;
        left = background.getDefaultX()-l/2;
        right = background.getDefaultX()+l/2;
        top = lyCounter+0.1f;
        bottom = background.getDefaultY()-h/2;
        if(tag.compareTo(HOME_WINDOW)==0){
            addOpenWindow(this);
        }
        TOTAL_WINS+=1f;
    }

    public boolean addIcon(ClickableIcon icon){
        if(numIcons<totalSize){
            //icon.setDefaultTrans(icon.getDefaultX(),icon.getDefaultY(),background.getDefaultZ()+0.1f);
            icons[numIcons] = icon;
            lyCounter-= icon.getHeight()+0.1f;
            numIcons++;
            return true;
        }else{
            return false;
        }
    }

    public boolean addIconLeft(ClickableIcon icon){
        if(numIcons<totalSize){
            lyCounter -= icon.getHeight()/2;
            float tLeft = left+icon.getLength()/2+0.2f;
            icon.setDefaultTrans(tLeft,lyCounter,icon.getDefaultZ());
            lyCounter-= icon.getHeight()/2+0.1f;
            icons[numIcons] = icon;
            numIcons++;
            return true;
        }else{
            return false;
        }
    }

    public boolean addIconRight(ClickableIcon icon){
        if(numIcons<totalSize){
            ryCounter -= icon.getHeight()/2;
            float tRight = right-icon.getLength()/2-0.2f;
            icon.setDefaultTrans(tRight,ryCounter,icon.getDefaultZ());
            ryCounter-= icon.getHeight()/2;
            icons[numIcons] = icon;
            numIcons++;
            return true;
        }else{
            return false;
        }
    }

    public void setTerminatingIcon(ClickableIcon icon, String tag){
        this.terminatingIcon = icon;
        if(tag.compareTo(L_LANE)==0) {
            lyCounter -= icon.getHeight() / 2 + 0.1f;
        }else{
            ryCounter -= icon.getHeight() / 2 + 0.1f;
        }
        //terminatingIcon.setDefaultTrans(0f,0f,NUM_OPEN_WIN);
    }

    public void setHomeWinVars(DPad d, Slider s){
        dpad = d;
        slider = s;
    }

    public void onTouchDown(float x, float y){
        if(tag.compareTo(HOME_WINDOW)==0){
            if(this.isOpened) {
                dpad.onTouchDown(x, y);
                slider.onTouchDown(x, y);
                for (int i = 0; i < numIcons; i++) {
                    if (icons[i].isClicked(x, y)) {
                        addOpenWindow(icons[i].getActivity());
                    }
                    //icons[i].getActivity().onTouchDown(x,y);
                }
            }
        }else {
            if (this.isOpened) {
                if (!background.isClicked(x, y)) {
                    this.isOpened = false;
                    closeTopWindow();
                }else if(terminatingIcon!=null){
                    if(terminatingIcon.isClicked(x,y)) {
                        this.isOpened = false;
                        closeTopWindow();
                    }
                }

                for (int i = 0; i < numIcons; i++) {
                    if(icons[i].isClicked(x, y)) {
                        if(icons[i].getActivity()!=null) {
                            addOpenWindow(icons[i].getActivity());
                        }
                    }
                }
            } else {

            }
        }
    }

    public void onSecondaryTouchDown(float x, float y){

    }

    public void onSecondaryTouchUp(float x, float y){

    }

    public void onTouchMove(float x, float y){
        if(tag.compareTo(HOME_WINDOW)==0){
            dpad.onTouchMove(x,y);
            slider.onTouchMove(x,y);
        }
    }

    public void onTouchUp(float x, float y){
        if(tag.compareTo(HOME_WINDOW)==0){
            dpad.onTouchUp(x,y);
            slider.onTouchUp(x,y);
        }
    }

    public void ondrawFrame(float[] mMVPMatrix){
        if(tag.compareTo(HOME_WINDOW)==0){
            if(this.isOpened) {
                dpad.draw(mMVPMatrix);
                slider.draw(mMVPMatrix);
                for (int i = 0; i < numIcons; i++) {
                    icons[i].draw(mMVPMatrix);
                }
            }
        }else {
            if (isOpened) {
                //background.draw(mMVPMatrix);
                if(terminatingIcon!=null){
                    terminatingIcon.draw(mMVPMatrix);
                }
                for (int i = 0; i < numIcons; i++) {
                    icons[i].draw(mMVPMatrix);
                }
            }
        }
    }



    public void launched(){
        /*if(this.tag.compareTo(FULL_SCREEN_ACTIVITY)==0){
            android.opengl.Matrix.orthoM(GLRenderer.mProjectionMatrix, 0, -GLRenderer.RATIO, GLRenderer.RATIO, -1, 1, 3, -100);
        }*/
        isOpened = true;
    }

    public void closed(){
        isOpened = false;
    }

    public void changeTransform(float x, float y, float z){
        background.changeTransform(x,y,z);
    }

    public void setDefaultTrans(float x, float y, float z){
        background.setDefaultTrans(x,y,z);
    }

    public void setScaleX(float s){ background.setScaleX(s);}
    public void setScaleY(float s){ background.setScaleY(s);}

    public static void drawWindows(float[] mMVPMatrix){
        // for(int i=0;i<NUM_OPEN_WIN;i++){
        //if(OPEN_WINDOWS[i].tag.compareTo(HOME_WINDOW)!=0){

        OPEN_WINDOWS[NUM_OPEN_WIN-1].ondrawFrame(mMVPMatrix);
        // }
        //}
    }

    public static void addOpenWindow(Window w){
        if(NUM_OPEN_WIN<5){
            OPEN_WINDOWS[NUM_OPEN_WIN] = w;
            //w.initializeWindow();
            w.launched();
            if(w.tag.compareTo(FULL_SCREEN_ACTIVITY)==0){
                //GLRenderer.PAUSED = true;
                for(int i=NUM_OPEN_WIN-1;i>=0;i--){
                    OPEN_WINDOWS[i].closed();
                }
            }
            NUM_OPEN_WIN ++;
        }
    }

    public static void closeTopWindow(){
        NUM_OPEN_WIN = NUM_OPEN_WIN-1;
        OPEN_WINDOWS[NUM_OPEN_WIN].closed();
        if(OPEN_WINDOWS[NUM_OPEN_WIN].tag.compareTo(FULL_SCREEN_ACTIVITY)==0){
            GLRenderer.PAUSED = false;
            OPEN_WINDOWS[NUM_OPEN_WIN].onActivityQuit();
            for(int i=NUM_OPEN_WIN-1;i>=0;i--){
                OPEN_WINDOWS[i].launched();
            }
        }
    }

    public static void checkTouchDown(float x, float y){
        //for(int i=NUM_OPEN_WIN-1;i>=0;i--){
           // if(OPEN_WINDOWS[NUM_OPEN_WIN-1].tag.compareTo(HOME_WINDOW)!=0){
                OPEN_WINDOWS[NUM_OPEN_WIN-1].onTouchDown(x,y);
           // }
       // }
    }

    public static void checkSecondaryTouchDown(float x, float y){
        //for(int i=NUM_OPEN_WIN-1;i>=0;i--){
        // if(OPEN_WINDOWS[NUM_OPEN_WIN-1].tag.compareTo(HOME_WINDOW)!=0){
        OPEN_WINDOWS[NUM_OPEN_WIN-1].onSecondaryTouchDown(x,y);
        // }
        // }
    }

    public static void checkSecondaryTouchUp(float x, float y){
        //for(int i=NUM_OPEN_WIN-1;i>=0;i--){
        // if(OPEN_WINDOWS[NUM_OPEN_WIN-1].tag.compareTo(HOME_WINDOW)!=0){
        OPEN_WINDOWS[NUM_OPEN_WIN-1].onSecondaryTouchUp(x,y);
        // }
        // }
    }

    public static void checkTouchMove(float x, float y){
        OPEN_WINDOWS[NUM_OPEN_WIN-1].onTouchMove(x,y);
    }

    public static void checkTouchUp(float x, float y){
        OPEN_WINDOWS[NUM_OPEN_WIN-1].onTouchUp(x,y);
    }

    public float getTop(){return this.top;}
    public float getBottom(){return this.bottom;}
    public float getLeft(){return this.left;}
    public float getRight(){return this.right;}
    public float getLLCounter(){return this.lyCounter;}
    public float getRLCounter(){return this.ryCounter;}
    public boolean isOpened(){return this.isOpened;}

    public void onActivityQuit(){

    }

    public void initializeWindow(){

    }
}
