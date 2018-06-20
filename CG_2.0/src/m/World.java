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
import m.poligonos.CGObject;
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
    
    private World() {       
        objetos    = new ArrayList();
        tempPoints = new ArrayList();
    }
    
    private World(List<CGObject> lista){
        objetos    = new ArrayList(lista);
        tempPoints = new ArrayList();
    }
    
    public void setPlanes(Vista...planes){
        this.vistas = new ArrayList<>();
        this.vistas.addAll(Arrays.asList(planes));
    }
    
    public void addObject(CGObject p){
        objetos.add(p);
        vistas.forEach((vista) -> {
            if (p instanceof Poligono)
                vista.addObject(new Poligono ( (Poligono) p) ); //Very ugly, but..
        });
    }
    
    public void addObject(CGObject...p){
        for (CGObject obj : p){
            addObject(obj);
        }
    }
    
    public void addObject(Collection<? extends CGObject> colecao){
        objetos.addAll(colecao);
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
    
    public List<CGObject> getObjects(){
        return objetos;
    }
    
    public void clearAll(){
        tempPoints.clear();
        objetos.clear();
        vistas.forEach((vista) -> {
            vista.clearAll();
        });
    }
    
}
