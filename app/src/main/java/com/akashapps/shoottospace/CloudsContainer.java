package com.akashapps.shoottospace;
import android.graphics.Color;
import android.opengl.Matrix;

import static com.akashapps.shoottospace.SpaceGameRenderer.context;

public class CloudsContainer {
    private TexturedPlane[] bgclouds;
    private float[] bgcloudSpeeds;
    private ParticleSystem cloudPS;
    public TexturedPlane darkCloud1, darkCloud2;
    public TexturedPlane back1;
    public TexturedCylinder c_cloud;
    private float cloudSpeed, cloud2speed, back1speed;
    
    public CloudsContainer(){
        darkCloud1 = new TexturedPlane(0.6f,0.3f,context,R.drawable.darkcloud);
        darkCloud2 = new TexturedPlane(1.0f,0.4f,context,R.drawable.darkcloud);
        back1 = new TexturedPlane(0.3f,0.05f,context,R.drawable.cloud_iii);
        darkCloud1.setDefaultTrans(1.0f,2.0f,2f);
        darkCloud2.setDefaultTrans(-1.0f,2.5f,2f);
        back1.setDefaultTrans(1.88f,0.1f,1f);
        
        cloudSpeed = 0.0006f;
        cloud2speed = 0.0009f;
        back1speed = 0.0006f;

        bgclouds = new TexturedPlane[10];
        bgcloudSpeeds = new float[10];
        for(int i=0;i<10;i++){
            float len = (float)Math.random()*0.5f;
            float hi = (float) Math.random()*0.1f;
            bgclouds[i] = new TexturedPlane(len,hi,context,R.drawable.cloud_iii);
            float y = (float)Math.random()*0.6f;
            y-=0.3f;
            bgclouds[i].setDefaultTrans(
                    (float)Math.random()*GLRenderer.RATIO*2-GLRenderer.RATIO,
                        y,
                    1f);
            bgcloudSpeeds[i] = (float)Math.random()*0.001f;
        }
        cloudPS = new ParticleSystem(new SimpleVector(0f,0f,2f),
                new SimpleVector(0f,0f,0f), Color.rgb(100,100,100),
                R.drawable.q_particle_iii,1000,50f,CustomParticles.VN_BLEND,
                0f,25f,0f);

        c_cloud = new TexturedCylinder(new SimpleVector(0f,0f,0f), 1,1.88f,
                0.6f,R.drawable.cylinder_cloud_ii);
        c_cloud.setDefaultTrans(0f,0.3f,0f);
        c_cloud.rotateX(-90);
    }
    
    public void onDrawFrameBG(float[] mMVPMatrix){
        back1.draw(mMVPMatrix);
        for(int i=0;i<10;i++){
           bgclouds[i].draw(mMVPMatrix);
        }
    }
    
    public void onDrawFrameFG(float[] mMVPMatrix){
        darkCloud1.draw(mMVPMatrix);
        darkCloud2.draw(mMVPMatrix);
    }

    public void drawCylinderCloud(float[] mMVPMatrix){
        c_cloud.draw(mMVPMatrix);
    }

    public void onUpdateFrame(){
        if(darkCloud1.getTransformX()>GLRenderer.RATIO){
            cloudSpeed = -0.0006f;
        }else if(darkCloud1.getTransformX()<-GLRenderer.RATIO){
            cloudSpeed = 0.0006f;
        }

        if(darkCloud2.getTransformX()>GLRenderer.RATIO){
            cloud2speed = -0.0009f;
        }else if(darkCloud2.getTransformX()<-GLRenderer.RATIO){
            cloud2speed = 0.0009f;
        }
        if(back1.transformX>GLRenderer.RATIO){
            back1speed = -0.0006f;
        }else if(back1.transformX<-GLRenderer.RATIO){
            back1speed = 0.0006f;
        }

        for(int i=0;i<10;i++){
            TexturedPlane curr = bgclouds[i];
            if(curr.transformX>GLRenderer.RATIO){
                bgcloudSpeeds[i] = -bgcloudSpeeds[i];
            }else if(curr.transformX<-GLRenderer.RATIO){
                bgcloudSpeeds[i] = -bgcloudSpeeds[i];
            }
            curr.updateTransform(bgcloudSpeeds[i],0f,0f);
        }
        
        darkCloud1.updateTransform(cloudSpeed,0f,0f);
        darkCloud2.updateTransform(cloud2speed,0f,0f);
        back1.updateTransform(back1speed,0f,0f);

        c_cloud.rotateZ(0.008f);
       // cloudPS.addParticlesAndDirection();
    }

    public void updateTransformFG(){

    }
        
}
