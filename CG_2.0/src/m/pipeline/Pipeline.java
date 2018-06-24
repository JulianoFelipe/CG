/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.List;
import java.util.Observer;
import m.poligonos.CGObject;

/**
 *
 * @author JFPS
 */
public interface Pipeline extends Observer{
    //Toma os objetos + vista (Para obter informações).
    default public void convert2D(List<CGObject> lista){
        lista.forEach((obj) -> {
            convert2D(obj);
        });
    }
    
    public void convert2D(CGObject object);
    
    default void reverseConversion(List<CGObject> lista){
        lista.forEach((obj) -> {
            reverseConversion(obj);
        });
    }
    
    public void reverseConversion(CGObject object);
}
