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
    public void convert2D(List<CGObject> lista);
    
    public void convert2D(CGObject object);
    
    public void reverseConversion(List<CGObject> lista);
    
    public void reverseConversion(CGObject object);
}
