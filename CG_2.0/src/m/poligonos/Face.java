/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class Face extends CGObject{ //extends CGObject{
    private List<Vertice> list;
    
    public Face (List<Vertice> lista){
        super();
        list = lista; //SHALLOW COPY Problem
    }
        
    public Face (Face f){
        super(f);
        list = new ArrayList(f.list.size()+5);
        f.getPoints().forEach((v) -> {
            list.add(new Vertice(v));
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
    }
    
    @Override
    public void updateInternals(CGObject updatedObj) {
        if (!(updatedObj instanceof Face)) throw new IllegalArgumentException("Não é uma instância de Face."); //Is this Right?

        Face updated = (Face) updatedObj;
        
        for (int i=0; i<list.size(); i++)
           list.get(i).copyAttributes(updated.get(i));
    }
}
