/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.Observable;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class CGViewport extends Observable {
    private Vertice min; //Window é a dos X;
    private Vertice max;

    public CGViewport(Vertice min, Vertice max) {
        this.min = min;
        this.max = max;
    }
    
    public CGViewport (Vertice max){
        this.max = max;
        this.min = new Vertice(0, 0);
    }

    public CGViewport(CGViewport toCopy) {
        this.min = toCopy.min;
        this.max = toCopy.max;
    }

    public void setMin(Vertice min) {
        this.min = min;
        setChanged();
        notifyObservers();
        clearChanged();
    }

    public void setMax(Vertice max) {
        this.max = max;
        setChanged();
        notifyObservers();
        clearChanged();
    }
    
    public void setDimensions(Vertice min, Vertice max){
        this.min = min;
        this.max = max;
        setChanged();
        notifyObservers();
        clearChanged();
    }
    
    public float getUmin() {
        return min.getX();
    }
    
    public float getUmax() {
        return max.getX();
    }
    
    public float getVmin() {
        return min.getY();
    }
    
    public float getVmax() {
        return max.getY();
    }
    
    public float getDeltaU(){
        return getUmax() - getUmin();
    }
    
    public float getDeltaV(){
        return getVmax() - getVmin();
    }
}
