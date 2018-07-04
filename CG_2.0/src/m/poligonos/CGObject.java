/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public abstract class CGObject implements Serializable{
    protected static long INSTANCES;
    protected final float[][] pointMatrix;
    protected long ID;
    
    protected float[] ka = {0,0,0}, ks = {0,0,0}, kd = {0,0,0,0}; 
    protected boolean isChromatic = true;
    
    protected CGObject(int numberOfPoints){ 
        this.ID = INSTANCES++;
        pointMatrix = new float [4][numberOfPoints];
    }
    
    protected CGObject(long ID, int numberOfPoints){ 
        this.ID = ID;
        pointMatrix = new float [4][numberOfPoints];
    }
    
    protected CGObject(float[][] pointMatrixCp, long ID) {
        this.ID = ID;
        
        pointMatrix = new float[4][pointMatrixCp[0].length]; //Se der erro não é matriz de pontos adequada
        for(int i = 0; i < pointMatrixCp[0].length; i++){
            pointMatrix[0][i] = pointMatrixCp[0][i];
            pointMatrix[1][i] = pointMatrixCp[1][i];
            pointMatrix[2][i] = pointMatrixCp[2][i];
            pointMatrix[3][i] = pointMatrixCp[3][i];
        }
    }
    
    protected CGObject(float[][] pointMatrix) {
        this(pointMatrix, INSTANCES++);
        //System.out.println("HERE!");
        //System.out.println(Arrays.deepToString(pointMatrix));
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
        final CGObject other = (CGObject) obj;
        return this.ID == other.ID;
    }
    
    @Override
    public String toString(){
        return "CGObject: ID=" + ID + "; Points=" + getNumberOfPoints() + ".";
    }

    public static long getINSTANCES() {
        return INSTANCES;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Get/Set points">
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
        pointMatrix[3][i] = 1;
    }
    
    public void setAll(float[][] pointMatrix){
        int newMatPoints = pointMatrix.length;
        
        if (newMatPoints == 2){
            for (int i=0; i<getNumberOfPoints(); i++){
                setPoint(i, pointMatrix[0][i], pointMatrix[1][i], 0);
            }
        } else if (newMatPoints == 3){
            for (int i=0; i<getNumberOfPoints(); i++){
                setPoint(i, pointMatrix[0][i], pointMatrix[1][i], pointMatrix[2][i]);
            }
        } else if (newMatPoints == 4) {
            for (int i=0; i<getNumberOfPoints(); i++){
                setPoint(i, pointMatrix[0][i], pointMatrix[1][i], pointMatrix[2][i]);
            }
        } else {
            throw new IllegalArgumentException("Matriz de pontos não possui número de coordenadas válido: " + newMatPoints);
        }
    }
    
    public Vertice getCentroide(){
        float maxX = pointMatrix[0][0],
                maxY = pointMatrix[1][0],
                maxZ = pointMatrix[2][0];
        
        float minX = pointMatrix[0][0],
                minY = pointMatrix[1][0],
                minZ = pointMatrix[2][0];
        
        for (int i=1; i<pointMatrix[0].length; i++){
            float x = pointMatrix[0][i],
                    y = pointMatrix[0][i],
                    z = pointMatrix[0][i];
            
            maxX = Math.max(maxX, x);
            minX = Math.min(minX, x);
            
            maxY = Math.max(maxY, y);
            minY = Math.min(minY, y);
            
            maxZ = Math.max(maxZ, z);
            minZ = Math.min(minZ, z);
        }
        
        return new Vertice( (float)((maxX+minX)/2), (float)((maxY+minY)/2), (float)((maxZ+minZ)/2));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Get/Set Ks">
    public boolean isChromatic() { return isChromatic; }
    
    public void setIsChromatic(boolean isAchromatic) { this.isChromatic = isAchromatic; }
    
    public Vertice getKa() { return new Vertice(ka[0], ka[1], ka[2]); }
    
    public void setKa(Vertice ka) { this.ka[0] = ka.getX(); this.ka[1] = ka.getY(); this.ka[2] = ka.getZ(); }
    
    public Vertice getKs() { return new Vertice(ks[0], ks[1], ks[2]); }
    
    public void setKs(Vertice ks) { this.ks[0] = ks.getX(); this.ks[1] = ks.getY(); this.ks[2] = ks.getZ(); }
    
    public Vertice getKd() { Vertice v = new Vertice(kd[0], kd[1], kd[2]); v.setW(kd[3]); return v; }
    
    public void setKd(Vertice kd) { this.kd[0] = kd.getX(); this.kd[1] = kd.getY(); this.kd[2] = kd.getZ(); this.kd[3] = kd.getW(); }
//</editor-fold>
}
