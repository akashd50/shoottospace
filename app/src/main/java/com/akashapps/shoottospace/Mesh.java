package com.akashapps.shoottospace;
public class Mesh {

    public GlLine[] meshlines;
    public Mesh(float[][] mesh){
        meshlines = new GlLine[mesh[0].length*2 +3];
        float z = 2f;
        float[] c = {0f,0f,1f,1f, 0f,0f,1f,1f};
        int k = 0;

        for(int i=0;i<mesh[0].length-1;i++){
            float[] temp2 = {mesh[0][i],mesh[2][i],z,1f,mesh[0][i+1],mesh[2][i+1],z,1f};
            GlLine line = new GlLine(temp2,c);
            meshlines[k] = line;
            k++;
        }
        float[] t = {mesh[0][mesh[0].length-1],mesh[2][mesh[0].length-1],z,1f,mesh[0][mesh[0].length-1],mesh[1][mesh[0].length-1],z,1f};
        meshlines[k] = new GlLine(t,c);
        k++;
        float[] c2 = {1f,0f,0f,1f, 1f,0f,0f,1f};
        for(int i=mesh[0].length-1;i>0;i--){
            float[] temp = {mesh[0][i],mesh[1][i],z,1f,mesh[0][i-1],mesh[1][i-1],z,1f};
            //float[] temp2 = {mesh[0][i],mesh[2][i],z,1f,mesh[0][i-1],mesh[2][i-1],z,1f};
            GlLine line = new GlLine(temp,c2);
            meshlines[k] = line;
            k++;
        }
        float[] temp2 = {mesh[0][0],mesh[1][0],z,1f,mesh[0][0],mesh[2][0],z,1f};
        meshlines[k] = new GlLine(temp2,c2);

    }

    public void drawMesh(float[] mMVPMatrix){
        for(int i=0;i<meshlines.length;i++){
            if(meshlines[i]!=null) {
                meshlines[i].draw(mMVPMatrix);
            }
        }
    }
}
