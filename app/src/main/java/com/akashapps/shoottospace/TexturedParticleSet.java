package com.akashapps.shoottospace;

public class TexturedParticleSet {

    protected TexturedPlane[] texturedPlanes;
    private int size;

    public TexturedParticleSet(int size){
        texturedPlanes = new TexturedPlane[size];
        this.size = size;
    }


}
