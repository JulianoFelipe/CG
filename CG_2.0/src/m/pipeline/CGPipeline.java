/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import m.Camera;
import m.Viewport;
import m.Visao;
import m.Window;
import m.poligonos.Vertice;
import utils.math.MMath;
import utils.math.VMath;

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
                
        matrixJP = new float[][] {
           {del_UX,      0, ((-xmin*del_UX)+umin)},
           {     0, del_VY, ((-ymin*del_VY)+vmax)},
           {     0,      0,                     1}
        };
    }
    
    protected final float[][] passThroughPipeline(float[][] pipeline3D, float[][] pipeline2D, float[][] pointMatrix){
        pointMatrix = multiply3D(pipeline3D, pointMatrix);
        return multiply2D(pipeline2D, pointMatrix);
    }
    
    protected final float[][] multiply3D(float[][] pipelineMatrix, float[][] pointMatrix){
        return MMath.multiplicar(pipelineMatrix, pointMatrix);
    }
    
    //Essa retorna por criar uma nova matriz com uma linha a menos
    protected final float[][] multiply2D(float[][] pipe2Dmatrix, float[][] pointMatrix){
        pointMatrix = MMath.removeFactor(pointMatrix);
        
        return MMath.multiplicar(matrixJP, pointMatrix);
    }
    
    public abstract Visao getVisao();
}
