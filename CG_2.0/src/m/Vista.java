/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import m.pipeline.CGPipeline;
import m.poligonos.CGObject;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class Vista {
    private static final Logger LOG = Logger.getLogger("CG_2.0");

    private final CGPipeline pipe;
    private List<CGObject> objetos;
    private List<Vertice> tempPoints;
    //private final World world;

    
    public Vista(CGPipeline pipeline) {
        this.pipe = pipeline;
        objetos = new ArrayList();
        tempPoints = new ArrayList<>();
    }
    
    public void addObject(CGObject p){
        pipe.convert2D(p);
        objetos.add(p);
    }
    
    public void addObject(CGObject...p){
        for (CGObject obj : p){
            addObject(obj);
        }
    }
    
    public void addTempPoint(Vertice p){
        pipe.convert2D(p);
        tempPoints.add(p);
        //System.out.println("TEMP POINT: " + tempPoints);
    }
    
    public void addTempPoint(Vertice...p){
        for (Vertice obj : p){
            addTempPoint(obj);
        }
    }
    
    public List<CGObject> get2Dobjects(){
        return objetos;
    }
    
    public List<Vertice> getTempPoints(){
        return tempPoints;
    }
    
    public void clearTempPoints(){
        tempPoints.clear();
    }
    
    public void clearAll(){
        clearTempPoints();
        objetos.clear();
    }
    
    public Visao getVisao(){
        return pipe.getVisao();
    }
}
