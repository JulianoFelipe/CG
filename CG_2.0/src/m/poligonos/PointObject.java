/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.util.ArrayList;
import java.util.List;
import m.Eixo;

/**
 *
 * @author JFPS
 */
@Deprecated
public class PointObject extends CGObject{
    protected List<Vertice> lista;
    
    public PointObject(PointObject p){
        super(p);
        lista = new ArrayList(p.getNumberOfPoints()+5);
        
        p.lista.forEach((v) -> {
            lista.add(new Vertice(v));
        });
    }
    
    public PointObject(float[][] pointMatrix){
        super();  
        lista = new ArrayList(pointMatrix[0].length);
        
        for (int i=0; i<pointMatrix[0].length; i++){
            lista.add(new Vertice(
                pointMatrix[0][i],
                pointMatrix[1][i],
                pointMatrix[2][i]
            ));
        }
    }
        
    protected PointObject(int numberOfPoints){ 
        super();
        
        lista = new ArrayList(numberOfPoints);
    }
    
    @Override
    public String toString(){
        return "PointObject: ID=" + ID + "; Points=" + getNumberOfPoints() + ".";
    }
    
    public static PointObject build(List<Vertice> lista){
        float[][] point = new float[4][lista.size()];
        
        for (int i=0; i<lista.size(); i++){
            Vertice ith = lista.get(i);
            point[0][i] = ith.getX();
            point[1][i] = ith.getY();
            point[2][i] = ith.getZ();
            point[3][i] = 1;
        }
        
        return new PointObject (point);
    }

    @Override
    public List<? extends Vertice> getPoints() {
        return lista;
    }

    @Override
    public int getNumberOfPoints() {
        return lista.size();
    }

    @Override
    public Vertice get(int i) {
        return lista.get(i);
    }

    @Override
    public void set(int i, Vertice point) {
        lista.get(i).copyAttributes(point);
    }

    @Override
    public void updateInternals(CGObject updatedObj) {
        for (int i=0; i<lista.size(); i++){
            lista.get(i).copyAttributes(updatedObj.get(i));
        }
    }

    @Override
    public boolean contains(float x, float y, Eixo axis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insideBoundingBox(float x, float y, Eixo axis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vertice getCentroide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
