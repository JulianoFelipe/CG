/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.Arrays;
import java.util.Observable;
import m.Camera;
import m.CGViewport;
import m.Visao;
import m.CGWindow;
import m.poligonos.Vertice;
import utils.config.StandardConfigWinView;
import utils.math.MMath;
import utils.math.VMath;

/**
 *
 * @author JFPS
 */
public abstract class CGPipeline extends Observable implements Pipeline{
    protected final Camera cam;
    protected final CGWindow window;
    protected final CGViewport viewport;
    
    protected float[][] matrixJP;
    protected float[] jpProportions;
    protected boolean sruSRCchanged=true;
    
    protected double zoom;
    
    protected CGPipeline(Camera cam, CGWindow window, CGViewport viewport) {
        this.cam = cam;
        this.cam.addObserver(this); //:c
        
        this.window = window;
        this.window.addObserver(this);
        
        this.viewport = viewport;
        this.viewport.addObserver(this);
        
        jpProportions = new float[2];
        updateMatrixJP();
        
        zoom = jpProportions[0];
    }
    
    public Camera getCamera() {
        return cam;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        //Por enquanto, qualquer update é tratado igual, passando pelo pipeline inteiro novamente
        
        if (o instanceof Camera){
            sruSRCchanged = true;
            
            setChanged();
            notifyObservers();
            clearChanged();
        } else if (o instanceof CGWindow){
            updateMatrixJP();
            
            setChanged();
            notifyObservers();
            clearChanged();
        } else if (o instanceof CGViewport){
            updateMatrixJP();
            
            setChanged();
            notifyObservers();
            clearChanged();
        } else {
            throw new IllegalArgumentException("Update não previsto no pipeline.");
        }
    }

    public float[][] getMatrixJP() {
        return matrixJP;
    }
    
    public float[][] getMatrizSRUsrc(){
        Vertice u   = cam.getVetorU();
        Vertice v   = cam.getVetorV();
        Vertice n   = cam.getVetorN();
        Vertice VRP = cam.getVRP();
           
        Vertice minusVRP = new Vertice(-VRP.getX(), -VRP.getY(), -VRP.getZ());
        
        return new float[][] {
            {u.getX(), u.getY(), u.getZ(), (float)VMath.produtoEscalar(minusVRP, u)},
            {v.getX(), v.getY(), v.getZ(), (float)VMath.produtoEscalar(minusVRP, v)},
            {n.getX(), n.getY(), n.getZ(), (float)VMath.produtoEscalar(minusVRP, n)},
            {       0,        0,        0,           1}
        };
        
        /*return new float[][] {
            {u.getX(), u.getY(), u.getZ(), ((-VRP.getX()*u.getX())+(-VRP.getY()*u.getY())+(-VRP.getZ()*u.getZ()))},
            {v.getX(), v.getY(), v.getZ(), ((-VRP.getX()*v.getX())+(-VRP.getY()*v.getY())+(-VRP.getZ()*v.getZ()))},
            {n.getX(), n.getY(), n.getZ(), ((-VRP.getX()*n.getX())+(-VRP.getY()*n.getY())+(-VRP.getZ()*n.getZ()))},
            {       0,        0,        0,           1}
        };*/
    }
    
    protected float[][] getMatrizSRCsru(){
        return MMath.invert4x4Matrix(getMatrizSRUsrc());
    }
    
    protected final void updateMatrixJP(){
        float umin   = viewport.getUmin(),
              deltaU = viewport.getDeltaU(),
              vmin   = viewport.getVmin(),
              vmax   = viewport.getVmax(),
              deltaV = viewport.getDeltaV();
              
        float xmin   = window.getXmin(),
              deltaX = window.getDeltaX(),
              ymin   = window.getYmin(),
              deltaY = window.getDeltaY();
        
        float del_UX = (deltaU/deltaX),
              del_VY = ((vmin-vmax)/deltaY); 
           
        jpProportions[0] = del_UX;
        jpProportions[1] = deltaV / deltaY;
        //zoom = jpProportions[0];
        
        //System.out.println("PROPORTIONS: " + Arrays.toString(jpProportions));
        
        matrixJP = new float[][] {
           {del_UX,      0, ((-xmin*del_UX)+umin)},
           {     0, del_VY, ((-ymin*del_VY)+vmax)},
           {     0,      0,                     1}
        };
    }
    
    public abstract Visao getVisao();
    
    public abstract float getDP();
    
    public abstract void setDP(float dp);
    
    public float getProportions(){
        return jpProportions[0];
    }

    public void setWindowDimensions(int largura, int altura){
        window.setDimensions(largura, altura);
    }
    
    public void zoom(double zoom){
        this.zoom += zoom;
        //System.out.println("Zoom: " + zoom +  " Prop: " + jpProportions[0] + " FACTOR: " + this.zoom);
        CGWindow STD_WINDOW_1 = StandardConfigWinView.STD_WINDOW_1;
        //System.out.println("WIN Larg: " + STD_WINDOW_1.getDeltaX() + " Altu: " + STD_WINDOW_1.getDeltaY());
        int largura = (int) (STD_WINDOW_1.getDeltaX() / this.zoom);
        int altura  = (int) (STD_WINDOW_1.getDeltaY() / this.zoom);
        //System.out.println("LARGURA: " + largura + " Altura: " + altura);
        window.setDimensions(largura, altura);
        
        //updateMatrixJP();
    }
}
