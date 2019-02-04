package com.akashapps.shoottospace;

public class SimpleVector {
    public float x, y,z;

    public SimpleVector(){

    }

    public SimpleVector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString(){
        return "["+x+", "+y+", "+z+"]";
    }
    public static float distanceXY(SimpleVector s1, SimpleVector s2){
        float x2_x1 = s2.x-s1.x;
        float y2_y1 = s2.y-s1.y;
        float dist = (float)Math.sqrt(x2_x1*x2_x1 - y2_y1*y2_y1);
        return dist;
    }

    public static float distanceXYPoints(float x1, float y1, float x2, float y2){
        float x2_x1 = x2-x1;
        float y2_y1 = y2-y1;
        float dist = (float)Math.sqrt(x2_x1*x2_x1 - y2_y1*y2_y1);
        return dist;
    }
}
