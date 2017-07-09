/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.visualizacao;

import Model.Vertice;

/**
 *
 * @author Juliano
 */
public class Projecao {
    private Vertice v = null; //Vertical
    private Vertice u = null; //Horizontal
    private float mPers[][] = new float[4][4];
    private float mOrtograficaVistaFrontal[][] = new float[4][4];
    private float mOrtograficaVistaLateral[][] = new float[4][4];
    private float mOrtograficaVistaTopo[][] = new float[4][4];
    private float mCamera[][] = new float[4][4];
    
    /*private float far;
    private float near;*/
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public Vertice getV() {
        return v;
    }
    
    public void setV(Vertice v) {
        this.v = v;
    }
    
    public Vertice getU() {
        return u;
    }
    
    public void setU(Vertice u) {
        this.u = u;
    }
    
    public float[][] getmPers() {
        return mPers;
    }
    
    public void setmPers(float[][] mPers) {
        this.mPers = mPers;
    }
    
    public float[][] getmOrtograficaVistaFrontal() {
        return mOrtograficaVistaFrontal;
    }
    
    public void setmOrtograficaVistaFrontal(float[][] mOrtograficaVistaFrontal) {
        this.mOrtograficaVistaFrontal = mOrtograficaVistaFrontal;
    }
    
    public float[][] getmOrtograficaVistaLateral() {
        return mOrtograficaVistaLateral;
    }
    
    public void setmOrtograficaVistaLateral(float[][] mOrtograficaVistaLateral) {
        this.mOrtograficaVistaLateral = mOrtograficaVistaLateral;
    }
    
    public float[][] getmOrtograficaVistaTopo() {
        return mOrtograficaVistaTopo;
    }
    
    public void setmOrtograficaVistaTopo(float[][] mOrtograficaVistaTopo) {
        this.mOrtograficaVistaTopo = mOrtograficaVistaTopo;
    }
    
    public float[][] getmCamera() {
        return mCamera;
    }
    
    public void setmCamera(float[][] mCamera) {
        this.mCamera = mCamera;
    }
//</editor-fold>
    
    
}
