/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
import m.pipeline.CGPipeline;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;

/**
 *
 * @author JFPS
 */
public class Vista implements Observer{
    private static final Logger LOG = Logger.getLogger("CG_2.0");

    private final CGPipeline pipe;
    private List<CGObject> objetos;
    private List<Vertice> tempPoints;
    private final World mundo;
    
    public Vista(CGPipeline pipeline) {
        this.pipe = pipeline;
        pipe.addObserver(this); // :/
        objetos = new ArrayList();
        tempPoints = new ArrayList<>();
        mundo = World.getInstance();
    }
    
    public void addObject(CGObject p){
        pipe.convert2D(p);
        objetos.add(p);
        
        Camera cam = pipe.getCamera();
        if (p instanceof HE_Poliedro){
            ((HE_Poliedro) p).updateVisibility(cam);
        }
    }
    
    public void addObject(CGObject...p){
        for (CGObject obj : p){
            addObject(obj);
        }
    }
    
    public void addObject(Collection<? extends CGObject> collection){
        for (CGObject obj : collection){
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
    
    public void addTempPoint(Collection<? extends Vertice> collection){
        collection.forEach((obj) -> {
            addTempPoint(obj);
        });
    }
    
    public void setObject(int index, CGObject obj){ 
        objetos.get(index).updateInternals(obj);
        pipe.convert2D(objetos.get(index));

        Camera cam = pipe.getCamera();
        if (obj instanceof HE_Poliedro){
            ((HE_Poliedro) objetos.get(index)).updateVisibility(cam);
        }
    }
    
    public void remove(CGObject obj){
        objetos.remove(obj);
    }
    
    public void setLastTempPoint(Vertice v){
        tempPoints.get(tempPoints.size()-1).copyAttributes(v);
        pipe.convert2D(tempPoints.get(tempPoints.size()-1));
    }
    
    public void setTempPoint(int index, Vertice v){
        tempPoints.get(index).copyAttributes(v);
        pipe.convert2D(tempPoints.get(index));
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

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CGPipeline){
            //mundo.updateAll();
            mundo.updateThis(this);
            
            /*Camera cam = ((CGPipeline) o).getCamera();
            for (CGObject obj : objetos){
                if (obj instanceof HE_Poliedro){
                    ((HE_Poliedro) obj).updateVisibility(cam);
                }
            }*/
        }
    }
    
    public Camera getPipelineCamera(){
        return pipe.getCamera();
    }
    
    public CGPipeline getPipe(){
        return pipe;
    }
    
    public void remove(long id){
        for (CGObject obj : objetos){
            if (obj.getID() == id){
                objetos.remove(obj);
                return;
            }
        }
    }
    
    public CGObject getObject(CGObject obj){
        int index = objetos.indexOf(obj);
        if (index != -1) return objetos.get(index);
        else return null;
    }
    
    public void update(CGObject obj){
        int index = objetos.indexOf(obj);
        if (index == -1) return;
        
        pipe.reverseConversion(obj);

        mundo.update(obj, this);
    }
}
