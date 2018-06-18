/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import m.Camera;
import m.Viewport;
import m.Window;
import m.anderson.Vertice;

/**
 *
 * @author JFPS
 */
public abstract class CGPipeline implements Pipeline{
    protected final Camera cam;
    protected final Window window;
    protected final Viewport viewport;
    
    protected float[][] matrixJP;
    
    protected CGPipeline(Camera cam, Window window, Viewport viewport) {
        this.cam = cam;
        this.cam.addObserver(this); //:c
        
        this.window = window;
        this.window.addObserver(this);
        
        this.viewport = viewport;
        this.viewport.addObserver(this);
        
        updateMatrixJP();
    }
    
    /*@Override
    public List<CGObject> convert2D(List<CGObject> lista, Vista vista) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    
    /*@Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    public float[][] getMatrixJP() {
        return matrixJP;
    }
    
    public float[][] getMatrizSRUsrc(){
        Vertice u   = cam.getVetorU();
        Vertice v   = cam.getVetorV();
        Vertice n   = cam.getVetorN();
        Vertice VRP = cam.getVRP();
           
        return new float[][] {
            {u.getX(), u.getY(), u.getZ(), -VRP.getX()},
            {v.getX(), v.getY(), v.getZ(), -VRP.getY()},
            {n.getX(), n.getY(), n.getZ(), -VRP.getZ()},
            {       0,        0,        0,           1}
        };
    }
    
    protected final void updateMatrixJP(){
        float umin   = viewport.getUmin(),
              deltaU = viewport.getDeltaU(),
              vmin   = viewport.getVmin(),
              deltaV = viewport.getDeltaV();
        
        float xmin   = window.getXmin(),
              deltaX = window.getDeltaX(),
              ymin   = window.getYmin(),
              deltaY = window.getDeltaY();
        
        float del_UX = (deltaU/deltaX),
              del_VY = (deltaV/deltaY); 
        
        matrixJP = new float[][] {
           {del_UX,      0, ((-xmin*del_UX)+umin)},
           {     0, del_VY, ((-ymin*del_VY)+vmin)},
           {     0,      0,                     1}
        };
    }
}
