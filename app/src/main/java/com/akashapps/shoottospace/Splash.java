package com.akashapps.shoottospace;

import android.graphics.Color;

public class Splash {
    public Projectile[] splash;
    private int numStreams;
    private boolean inProgress;

    public Splash(int numParticleStreams){
        numStreams = numParticleStreams;
        splash = new Projectile[numStreams];
        float var = (float)Math.PI/numStreams;
        float s = 0f;
        for(int i=0;i<numStreams;i++){
            splash[i] = new Projectile(new SimpleVector(0f,0f,2f), 0.002f, s+=var,
                    200, 3f, Color.rgb(0.5f,0.6f,0.1f));
        }
        inProgress = false;
    }

    public void startSplashAnimation(SimpleVector location){
        inProgress = true;
        for(int i=0;i<numStreams;i++){
            splash[i].shootProjectileAngled(location);
        }
    }

    public void ondrawFrame(float[] mMVPMatrix){
        if(inProgress) {
            for (int i = 0; i < numStreams; i++) {
                splash[i].onDrawFrame(mMVPMatrix);
            }
        }
    }

    public void stopAnimation(){
        inProgress = false;
        for(int i=0;i<numStreams;i++){
            splash[i].inProgress = false;
        }
    }
}
