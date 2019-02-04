package com.akashapps.shoottospace;

public class Sphere {

    private float[] coordinates, triangleFanTop, triangleFanBottom;
    public Sphere(float r, int q, int numStripes){
        float verticalAngle=0;
        float numAngles=0;
        int numPoints = 360/q;
        float top = r/2;
        float bottom = -r/2;
        triangleFanBottom = new float[6 + numPoints*3];
        triangleFanTop = new float[6+numPoints*3];
        coordinates = new float[numPoints*(numStripes-2)];
        int cordOffset = 0;
        if(numStripes%2==0){
            numAngles = 90/(numStripes/2);
            verticalAngle = 0f;
            for(int i=0;i<numStripes/2;i++){
                float angle = ((float) i / (float) 360)
                        * ((float) Math.PI * 2f);
                float base = (float)(r* Math.cos(angle));
                float height = (float)(r* Math.sin(angle));
                coordinates[cordOffset++] = base;
                coordinates[cordOffset++] = height;
                coordinates[cordOffset++] = 0-r/2;

            }
        }else{

        }
    }

}
