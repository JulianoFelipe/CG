/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import m.Eixo;

/**
 *
 * @author JFPS
 */
public abstract class CGObject implements Serializable{
    protected static long INSTANCES;
    protected final long ID;
    
    protected float[] ka, kd, ks; 
    protected boolean isChromatic = true;
    protected boolean setK=false;
    
    protected transient BooleanProperty changedProperty = new SimpleBooleanProperty(false);
    
    protected CGObject(){ 
        this.ID = INSTANCES++;
    }
    
    protected CGObject(CGObject obj){
        this.ID = obj.ID;
    }
    
    protected CGObject(long id){
        this.ID = id;
    }
    
    public abstract List<? extends Vertice> getPoints();
       
    public abstract int getNumberOfPoints(); 
    
    public abstract Vertice get(int i);
    
    public abstract void set(int i, Vertice point);

    public abstract boolean contains(float x, float y, Eixo axis);
    
    public abstract boolean insideBoundingBox(float x, float y, Eixo axis);
    
    public abstract void updateInternals(CGObject updatedObj);
    
    public boolean equalPoints(CGObject that){
        if (this.getNumberOfPoints() != that.getNumberOfPoints())
            return false;
        
        for (int i=0; i<this.getNumberOfPoints(); i++){
            if (!this.get(i).equalPoints(that.get(i)))
                return false;
        }
        return true;
    }
    
    public BooleanProperty changedProperty() {
        return changedProperty;
    }
    
    public abstract Vertice getCentroide();
    
    public long getID(){
        return ID;
    }
    
    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.ID ^ (this.ID >>> 32));
        return hash;
    }
    
    @Override
    public final boolean equals(Object obj) {
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
    
    //<editor-fold defaultstate="collapsed" desc="Get/Set Ks">
    public boolean isChromatic() { return isChromatic; }
    
    public void setIsChromatic(boolean isChromatic) {this.isChromatic = isChromatic; }
    
    public float[] getKa() { if (!setK) return null; else return new float[]{ka[0], ka[1], ka[2]}; }
        
    public float[] getKd() { if (!setK) return null; else return new float[] {kd[0], kd[1], kd[2]}; }
    
    public float[] getKs() { if (!setK) return null; else return new float[] {ks[0], ks[1], ks[2], ks[3]}; }
    
    /*
    public void setKa(float kaRed, float kaGreen, float kaBlue) {
        if (setK){
            this.ka[0] = kaRed; this.ka[1] = kaGreen; this.ka[2] = kaBlue; 
        } else {
            setK = true;
            ka = new float[]{
                kaRed, kaGreen, kaBlue
            };
        }
        changedProperty.set(true);
    }
    
    public void setKd(float kdRed, float kdGreen, float kdBlue) {
        if (setK){
            this.kd[0] = kdRed; this.kd[1] = kdGreen; this.kd[2] = kdBlue; 
        } else {
            setK = true;
            kd = new float[]{
                kdRed, kdGreen, kdBlue
            };
        } 
        changedProperty.set(true);
    }
    
    public void setKd(float ksRed, float ksGreen, float ksBlue, float ksn) { 
        if (setK){
            this.ks[0] = ksRed; this.ks[1] = ksGreen; this.ks[2] = ksBlue; this.ks[3] = ksn; 
        } else {
            setK = true;
            ks = new float[]{
                ksRed, ksGreen, ksBlue, ksn
            };
        }
        changedProperty.set(true);
    }*/
    
    public void setAllK(float[] ka, float[] kd, float[] ks){
        if (setK){
            this.ka[0] = ka[0]; this.ka[1] = ka[1]; this.ka[2] = ka[2]; 
            this.kd[0] = kd[0]; this.kd[1] = kd[1]; this.kd[2] = kd[2]; 
            this.ks[0] = ks[0]; this.ks[1] = ks[1]; this.ks[2] = ks[2]; this.ks[3] = ks[3]; 
        } else {
            if (ka==null || kd==null || ks==null) return;
            
            setK = true;
            
            this.ka = new float[]{
                ka[0], ka[1], ka[2]
            };
            this.kd = new float[]{
                kd[0], kd[1], kd[2]
            };
            this.ks = new float[]{
                ks[0], ks[1], ks[2], ks[3]
            };
        }
        changedProperty.set(true);
    }
    
    public boolean isKset(){ return setK; }
//</editor-fold>
}
