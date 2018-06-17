/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.Observable;
import m.Camera;
import m.anderson.Vertice;

/**
 *
 * @author JFPS
 */
public abstract class CGPipeline implements Pipeline{
    protected final Camera cam;
    
    protected CGPipeline(Camera cam) {
        this.cam = cam;
        cam.addObserver(this); //:c
    }
    
    /*@Override
    public List<CGObject> convert2D(List<CGObject> lista, Vista vista) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    
    /*@Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    
    protected float[][] getMatrizSRUsrc(){
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
}
