/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.Observable;

/**
 *
 * @author JFPS
 */
public class CGWindow extends Observable{
    private int altura;
    private int largura;

    public CGWindow(int largura, int altura) {
        this.altura = altura;
        this.largura = largura;
    }
   
    public CGWindow(CGWindow toCopy){
        this.altura = toCopy.altura;
        this.largura = toCopy.largura;
    }
    
    public void setLargura(int largura) {
        this.largura = largura;
        setChanged();
        notifyObservers();
        clearChanged();
    }

    public void setAltura(int altura) {
        this.altura = altura;
        setChanged();
        notifyObservers();
        clearChanged();
    }
    
    public void setDimensions(int largura, int altura){
        this.largura = largura;
        this.altura = altura;
        setChanged();
        notifyObservers();
        clearChanged();
    }
    
    public int getXmin() {
        return -(largura/2);
    }
    
    public int getXmax() {
        return (largura/2);
    }
    
    public int getYmin() {
        return -(altura/2);
    }
    
    public int getYmax() {
        return (altura/2);
    }
    
    public float getDeltaX(){
        return largura;
    }
    
    public float getDeltaY(){
        return altura;
    }
}
