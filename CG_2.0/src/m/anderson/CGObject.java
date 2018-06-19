/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.anderson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public abstract class CGObject implements Serializable{
    protected static long INSTANCES;
    protected float[][] pointMatrix;
    protected long ID;
    
    protected CGObject(){ this.ID = INSTANCES++; }
    
    protected CGObject(long ID){ this.ID = ID; }
    
    protected CGObject(float[][] pointMatrixCp, long ID) {
        this.ID = ID;
        
        pointMatrix = new float[4][pointMatrixCp[0].length]; //Se der erro não é matriz de pontos adequada
        for(int i = 0; i < pointMatrixCp.length; i++){
            float[] aMatrix = pointMatrixCp[i];
            int   aLength = aMatrix.length;
            this.pointMatrix[i] = new float[aLength];
            System.arraycopy(aMatrix, 0, pointMatrixCp[i], 0, aLength);
        }
    }
    
    protected CGObject(float[][] pointMatrix) {
        this(pointMatrix, INSTANCES++);
    }
    
    protected void setPointMatrix(float[][] pointMatrix){
        this.pointMatrix = pointMatrix;
    }
    
    public float[][] getPointMatrix(){
        return pointMatrix;
    }
    
    public List<Vertice> getPointList(){
        List<Vertice> lista = new ArrayList();
        for (int i=0; i<pointMatrix[0].length; i++){
            lista.add(new Vertice(pointMatrix[0][i],
                                  pointMatrix[1][i],
                                  pointMatrix[2][i]));
        }
        
        return lista;
    }
    
    public int getNumberOfPoints(){
        return pointMatrix[0].length;
    }
    
    public long getID(){
        return ID;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.ID ^ (this.ID >>> 32));
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Poligono other = (Poligono) obj;
        return this.ID == other.ID;
    }
    
    @Override
    public String toString(){
        return "CGObject: ID=" + ID + "; Points=" + getNumberOfPoints() + ".";
    }

    public static long getINSTANCES() {
        return INSTANCES;
    }
    
    public Vertice getPoint(int i){
        return new Vertice(pointMatrix[0][i],
                           pointMatrix[1][i],
                           pointMatrix[2][i]);
    }
    
    public void setPoint(int i, Vertice v){
        pointMatrix[0][i] = v.getX();
        pointMatrix[1][i] = v.getY();
        pointMatrix[2][i] = v.getZ();
        pointMatrix[3][i] = v.getW();
    }
    
    public void setPoint(int i, float x, float y, float z){
        pointMatrix[0][i] = x;
        pointMatrix[1][i] = y;
        pointMatrix[2][i] = z;
    }
}
