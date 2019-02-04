package com.akashapps.shoottospace.minigame1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.akashapps.shoottospace.GLRenderer;
import com.akashapps.shoottospace.GLRendererView;
import com.akashapps.shoottospace.TouchController;
public class LiftOffView extends GLSurfaceView {
    private final LiftOffRenderer mRenderer;
    //private TouchController touchController;
    //private Utilities utilities;
    public LiftOffView(Context context){
        super(context);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        // Create an OpenGL ES 2.0 context
        //setEGLContextClientVersion(2);
        //utilities = new Utilities(context);

      //  touchController = new TouchController();
       // this.setEGLContextClientVersion(2);
        //this.setEGLConfigChooser(new AAConfigChooser(this));
        int uiOptions = this.SYSTEM_UI_FLAG_FULLSCREEN;
        this.setSystemUiVisibility(uiOptions);
        /*this.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
                // back to Pixelflinger on some device (read: Samsung I7500)
                int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });*/

        mRenderer = new LiftOffRenderer(context, GLRendererView.touchController);


        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                GLRendererView.touchController.touchDown(event);
                break;
            // invalidate();
            case MotionEvent.ACTION_UP:
                GLRendererView.touchController.touchUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                GLRendererView.touchController.touchMovement(event);
                break;
            // case MotionEvent.
        }
        //invalidate();
        return true;
    }
}
