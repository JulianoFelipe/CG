/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.List;
import java.util.Observer;
import m.poligonos.CGObject;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public interface Pipeline extends Observer{
    //Toma os objetos + vista (Para obter informações).
    public void convert2D(Vertice v);
    
    default public void convert2D(CGObject object){
        object.getPoints().forEach((v) -> {
            convert2D(v);
        });
    }
    
    default public void convert2D(List<CGObject> lista){
        lista.forEach((obj) -> {
            convert2D(obj);
        });
    }
    
    public void reverseConversion(Vertice v);
    
    default public void reverseConversion(CGObject object){
        object.getPoints().forEach((v) -> {
            reverseConversion(v);
        });
    }
    
    default void reverseConversion(List<CGObject> lista){
        lista.forEach((obj) -> {
            reverseConversion(obj);
        });
    }
}
