/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.Observable;
import m.anderson.Vertice;

/**
 *
 * @author JFPS
 */
public class Viewport extends Observable {
    private Vertice min; //Window é a dos X;
    private Vertice max;

    public Viewport(Vertice min, Vertice max) {
        this.min = min;
        this.max = max;
    }
    
    public Viewport (Vertice max){
        this.max = max;
        this.min = new Vertice(0, 0);
    }

    public void setMin(Vertice min) {
        this.min = min;
        notifyObservers();
    }

    public void setMax(Vertice max) {
        this.max = max;
        notifyObservers();
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
