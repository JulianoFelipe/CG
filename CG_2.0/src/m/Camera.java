/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;


import java.util.Observable;
import m.poligonos.Vertice;
import utils.math.VMath;

/**
 *
 * @author JFPS
 */
public class Camera extends Observable{
    private Vertice ViewUp;
    private Vertice VRP;
    private Vertice P;
    
    private boolean changed = false;
    
    private Vertice nNormalizado;
    private Vertice vNormalizado;
    private Vertice uNormalizado;

    public Camera(Vertice ViewUp, Vertice VRP, Vertice P) {
        this.ViewUp = ViewUp;
        this.VRP = VRP;
        this.P = P;
        /*this.ViewUp = new Vertice(ViewUp);
        this.VRP    = new Vertice(VRP);
        this.P      = new Vertice(P);*/
        calculateNVU();
    }

    public Camera(Camera toCopy) {
        this.ViewUp = new Vertice(toCopy.ViewUp);
        this.VRP    = new Vertice(toCopy.VRP);
        this.P      = new Vertice(toCopy.P);
        calculateNVU();
    }

    public void setViewUp(Vertice ViewUp) {
        this.ViewUp = ViewUp;
        changed = true;
        setChanged(); //??????
        notifyObservers();
        clearChanged(); //?????
    }

    public void setVRP(Vertice VRP) {
        this.VRP = VRP;
        changed = true;
        setChanged(); //??????
        notifyObservers();
        clearChanged(); //?????
    }
    
    public Vertice getVRP(){
        return VRP;
    }

    public void setP(Vertice P) {
        this.P = P;
        changed = true;
        setChanged(); //??????
        notifyObservers();
        clearChanged(); //?????
    }

    public Vertice getVetorN() {
        if (changed == true){      
            calculateNVU();
            changed = false;
        }
        
        return nNormalizado;
    }
    
    public Vertice getVetorV() {
        if (changed == true){
            calculateNVU();
            changed = false;
        }
        
        return vNormalizado;
    }

    public Vertice getVetorU() {
        if (changed == true){
            calculateNVU();
            changed = false;
        }
        
        return uNormalizado;
    }

    private void calculateNVU(){
        nNormalizado = new Vertice(VRP.getX()-P.getX(), VRP.getY()-P.getY(), VRP.getZ()-P.getZ());
        VMath.normalizar(nNormalizado);
        
        double escalar = VMath.produtoEscalar(ViewUp, nNormalizado);
        Vertice vTemp = VMath.produto(nNormalizado, escalar);
        vNormalizado = new Vertice(
                            ViewUp.getX() - vTemp.getX(),
                            ViewUp.getY() - vTemp.getY(),
                            ViewUp.getZ() - vTemp.getZ());
        VMath.normalizar(vNormalizado);
        
        uNormalizado = VMath.produtoVetorial(vNormalizado, nNormalizado);
    }

    public Vertice getViewUp() {
        return ViewUp;
    }

    public Vertice getP() {
        return P;
    }
    
    public void set(Camera cam){
        this.VRP = cam.VRP;
        this.ViewUp = cam.ViewUp;
        this.P = cam.P;
        changed = true;

        setChanged(); //??????
        notifyObservers();
        clearChanged(); //?????
    }

    @Override
    public String toString() {
        return "Camera{" + "ViewUp=" + ViewUp + ", VRP=" + VRP + ", P=" + P + '}';
    }
    
    public static void main(String...args){
        Camera cam = new Camera( new Vertice( 0, 1, 0), new Vertice( 10, 0, 0), new Vertice(0, 0, 0) );
        
        System.out.println(cam.getVetorN());
        System.out.println(cam.getVetorU());
        System.out.println(cam.getVetorV());
    }
}
