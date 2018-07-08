/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import m.poligonos.Aresta;
import m.poligonos.CGObject;
import m.poligonos.Face;
import m.poligonos.Nregular;
import m.poligonos.PointObject;
import m.poligonos.Poligono;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class World {
    private static final Logger LOG = Logger.getLogger("CG_2.0");    
    private final List<CGObject> objetos;
    private final List<Vertice> tempPoints;
    private List<Vista> vistas;
    
    private final List<CGObject> axis = new ArrayList<>();
    private boolean addedAxisFlag = false;
    
    private World() {       
        objetos    = new ArrayList();
        tempPoints = new ArrayList();
        
        Aresta x_axis = new Aresta(new Vertice(0,0,0), new Vertice(5000,0,0));
        Aresta y_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,5000,0));
        Aresta z_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,0,5000));
        
        Aresta mx_axis = new Aresta(new Vertice(0,0,0), new Vertice(-5000,0,0));
        Aresta my_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,-5000,0));
        Aresta mz_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,0,-5000));
        
        axis.add( x_axis);
        axis.add( y_axis);
        axis.add( z_axis);
        axis.add(mx_axis);
        axis.add(my_axis);
        axis.add(mz_axis);
    }
    
    private World(List<CGObject> lista){
        objetos    = new ArrayList(lista);
        tempPoints = new ArrayList();
        
        Aresta x_axis = new Aresta(new Vertice(0,0,0), new Vertice(5000,0,0));
        Aresta y_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,5000,0));
        Aresta z_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,0,5000));
        
        Aresta mx_axis = new Aresta(new Vertice(0,0,0), new Vertice(-5000,0,0));
        Aresta my_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,-5000,0));
        Aresta mz_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,0,-5000));
        
        axis.add( x_axis);
        axis.add( y_axis);
        axis.add( z_axis);
        axis.add(mx_axis);
        axis.add(my_axis);
        axis.add(mz_axis);
    }
    
    public void setPlanes(Vista...planes){
        this.vistas = new ArrayList<>();
        this.vistas.addAll(Arrays.asList(planes));
    }
    
    public void addObject(CGObject p){
        objetos.add(p);
        vistas.forEach((vista) -> {
            vista.addObject(deepCopy(p)); //Very ugly, but..
        });
    }
    
    public void addObject(CGObject...p){
        for (CGObject obj : p){
            addObject(obj);
        }
    }
    
    public void addObject(Collection<? extends CGObject> colecao){
        colecao.forEach((obj) -> {
            addObject(obj);
        });
    }
    
    public void addTempPoint(Vertice p){
        tempPoints.add(p);
        vistas.forEach((vista) -> {
            vista.addTempPoint(new Vertice(p));
        });
        //System.out.println("TEMPO POINT: " + tempPoints);
    }
    
    public void addTempPoint(Vertice...p){
        for (Vertice obj : p){
            addTempPoint(obj);
        }
    }
    
    public void clearTemp(){
        tempPoints.clear();
        vistas.forEach((vista) -> {
            vista.clearTempPoints();
        });
    }

    public List<Vista> getVistas() {
        return vistas;
    }
    
    private static World INSTANCE;
    
    public static World getInstance(){
        if (INSTANCE == null){
            INSTANCE = new World();
        }
        return INSTANCE;
    }
    
    public List<CGObject> getObjectsCopy(){
        List<CGObject> newList = new ArrayList<>(objetos.size());
        objetos.forEach((v) -> {
            newList.add(deepCopy(v));
        });
        
        return newList;
    }
    
    public List<Vertice> getTempPointsCopy(){
        List<Vertice> newList = new ArrayList<>(tempPoints.size());
        tempPoints.forEach((v) -> {
            newList.add(new Vertice(v));
        });
        
        return newList;
    }
    
    private CGObject deepCopy(CGObject obj){
        CGObject deepCopied = null;
        
        if        (obj instanceof Vertice){
            deepCopied = new Vertice( (Vertice) obj);
        } else if (obj instanceof Aresta){
            deepCopied = new Aresta( (Aresta) obj);
        } else if (obj instanceof Face){
            deepCopied = new Face( (Face) obj);
        } else if (obj instanceof Poligono){
            deepCopied = new Poligono( (Poligono) obj);
        } else if (obj instanceof Nregular){
            deepCopied = new Nregular( (Nregular) obj);
        } else if (obj instanceof PointObject){
            deepCopied = new PointObject( (PointObject) obj);
        } else {
            throw new IllegalArgumentException("Sub-tipo de CGObject não previsto para cópia.");
        }
        
        return deepCopied;
    }
    
    public void clearAll(){
        tempPoints.clear();
        objetos.clear();
        vistas.forEach((vista) -> {
            vista.clearAll();
        });
        
        if (addedAxisFlag){
            addedAxisFlag = false;
            addAxis();
        }
    }    
    
    public void addAxis(){
        if (addedAxisFlag) return;
        
        addObject(axis);
        addedAxisFlag = true;
    }
    
    public void removeAxis(){
        axis.forEach((CGObject axisObj) -> {
            removeObject(axisObj.getID());
            vistas.forEach((vista) -> {
                vista.remove(axisObj.getID());
            });
        });
        
        addedAxisFlag = false;
    }
    
    public void removeObject(long id){
        for (CGObject obj : objetos){
            if (obj.getID() == id){
                objetos.remove(obj);
                return;
            }
        }
    }
}
