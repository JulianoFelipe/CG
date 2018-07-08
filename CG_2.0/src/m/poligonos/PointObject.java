/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.util.List;

/**
 *
 * @author JFPS
 */
public class PointObject extends CGObject{
    public PointObject(PointObject p){
        super(p.getPointMatrix(), p.getID());
    }
    
    public PointObject(float[][] pointMatrix, long ID){
        super(pointMatrix, ID);    
    }
    
    public PointObject(float[][] pointMatrix){
        super(pointMatrix);     
    }
    
    protected PointObject(long ID, int numberOfPoints){ super(ID,numberOfPoints); }
    
    protected PointObject(int numberOfPoints){ super(numberOfPoints); }
    
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
}
