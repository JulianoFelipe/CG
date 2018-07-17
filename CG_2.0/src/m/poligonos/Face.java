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
public class Face extends CGObject{ //extends CGObject{
    private List<Vertice> list;
    
    private float min_x, max_x,
                  min_y, max_y,
                  min_z, max_z;
    
    public Face (List<Vertice> lista){
        super();
        list = lista; //SHALLOW COPY Problem
        
        list.forEach((v) -> {
            
            min_x = Math.min(v.getX(), min_x);
            max_x = Math.max(v.getX(), max_x);
            min_y = Math.min(v.getY(), min_y);
            max_y = Math.max(v.getY(), max_y);
            min_z = Math.min(v.getZ(), min_z);
            max_z = Math.max(v.getZ(), max_z);
        });
    }
        
    public Face (Face f){
        super(f);
        list = new ArrayList(f.list.size()+5);
        f.getPoints().forEach((v) -> {
            list.add(new Vertice(v));
            
            min_x = Math.min(v.getX(), min_x);
            max_x = Math.max(v.getX(), max_x);
            min_y = Math.min(v.getY(), min_y);
            max_y = Math.max(v.getY(), max_y);
            min_z = Math.min(v.getZ(), min_z);
            max_z = Math.max(v.getZ(), max_z);
        });
    }
   
    @Override
    public String toString(){
        return "Face: ID=" + ID + "; Points=" + getNumberOfPoints() + ".";
    }

    @Override
    public List<Vertice> getPoints() {
        return list;
    }

    @Override
    public int getNumberOfPoints() {
        return list.size();
    }

    @Override
    public Vertice get(int i) {
        return list.get(i);
    }

    @Override
    public void set(int i, Vertice point) {
        list.get(i).copyAttributes(point);
        
        min_x = Math.min(point.getX(), min_x);
        max_x = Math.max(point.getX(), max_x);
        min_y = Math.min(point.getY(), min_y);
        max_y = Math.max(point.getY(), max_y);
        min_z = Math.min(point.getZ(), min_z);
        max_z = Math.max(point.getZ(), max_z);
    }
    
    @Override
    public void updateInternals(CGObject updatedObj) {
        if (!(updatedObj instanceof Face)) throw new IllegalArgumentException("Não é uma instância de Face."); //Is this Right?

        Face updated = (Face) updatedObj;
        
        for (int i=0; i<list.size(); i++){
           list.get(i).copyAttributes(updated.get(i));
        
            Vertice point = updated.get(i);
            min_x = Math.min(point.getX(), min_x);
            max_x = Math.max(point.getX(), max_x);
            min_y = Math.min(point.getY(), min_y);
            max_y = Math.max(point.getY(), max_y);
            min_z = Math.min(point.getZ(), min_z);
            max_z = Math.max(point.getZ(), max_z);
        }
    }

    @Override
    public boolean contains(float x, float y, Eixo axis) {
        //https://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon
        
        if (!insideBoundingBox(min_x, min_y, axis)) return false;
        
        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for (int i=0, j=getNumberOfPoints()-1 ; i<getNumberOfPoints() ; j=i++){
            Vertice ith = get(i);
            Vertice jth = get(j);
            
            if ( (ith.getY()>y) != (jth.getY()>y) && x < (jth.getX()-ith.getX()) * (y-ith.getY()) / (jth.getY()-ith.getY()) + ith.getX() ) {
                inside = !inside;
            }
        }

        return inside;
    }

    @Override
    public boolean insideBoundingBox(float x, float y, Eixo axis) {
        float minX, maxX, minY, maxY;
        switch (axis){
            case Eixo_XY:
                minX = min_x;
                maxX = max_x;
                minY = min_y;
                maxY = max_y;
                break;
            case Eixo_XZ:
                minX = min_x;
                maxX = max_x;
                minY = min_z;
                maxY = max_z;
                break;
            case Eixo_YZ:
                minX = min_y;
                maxX = max_y;
                minY = min_z;
                maxY = max_z;
                break;
            default :
                throw new IllegalArgumentException("Eixo " + axis + " não é 2D.");
        }
        
        return !(x < minX || x > maxX || y < minY || y > maxY); //Se menor que min ou maior que max, false
    }

    @Override
    public Vertice getCentroide() {
        double avgX=0, avgY=0, avgZ=0;
        
        for (Vertice v : list){
            avgX += v.getX();
            avgY += v.getY();
            avgZ += v.getZ();
        }
        
        int count = list.size();
        
        return new Vertice(
            (float) (avgX/count),
            (float) (avgY/count),
            (float) (avgZ/count)
        );
    }
}
