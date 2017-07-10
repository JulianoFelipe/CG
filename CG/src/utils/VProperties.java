/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Poligono;
import Model.Vertice;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class VProperties {
    public static Vertice getMaxVertex(List<Poligono> lista){
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        
        for (Poligono pol : lista){
            for (Vertice ver : pol.getVertices()){
                if (ver.getX() > maxX)
                    maxX = ver.getX();
                
                if(ver.getY() > maxY)
                    maxY = ver.getY();
            }
        }
        
        return new Vertice((float) maxX, (float) maxY);
    }
}
