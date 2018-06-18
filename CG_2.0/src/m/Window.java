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
public class Window extends Observable{
    private int altura;
    private int largura;

    public Window(int largura, int altura) {
        this.altura = altura;
        this.largura = largura;
    }
   
    public void setLargura(int largura) {
        this.largura = largura;
        notifyObservers();
    }

    public void setAltura(int altura) {
        this.altura = altura;
        notifyObservers();
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
