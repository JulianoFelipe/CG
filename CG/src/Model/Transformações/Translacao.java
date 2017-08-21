/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Transformações;

import Model.Eixo;
import Model.Poligono;
import Model.Vertice;
import java.util.List;


/**
 *
 * @author JFPS
 */
public class Translacao{
    
    public Poligono transladar(int unidadesX, int unidadesY, int unidadesZ, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            p.getVertices().get(i).setX((float) (copy.getX() + unidadesX));
            p.getVertices().get(i).setY((float) (copy.getY() + unidadesY));
            //p.getVertices().get(i).setZ((float) (copy.getZ() + unidadesZ));
        }
        
        return p;
    }
    
    public Poligono transladar(Eixo axis, int unidades, Poligono p){
        int unidadesX=0, unidadesY=0, unidadesZ=0;
        switch(axis){
            case Eixo_X:   unidadesX = unidades; break;
            case Eixo_Y:   unidadesY = unidades; break;
            case Eixo_Z:   unidadesZ = unidades; break;
            case Eixo_XY:  unidadesX = unidadesY = unidades; break;
            case Eixo_XZ:  unidadesX = unidadesZ = unidades; break;
            case Eixo_YZ:  unidadesY = unidadesZ = unidades; break;
            case Eixo_XYZ: unidadesX = unidadesY = unidadesZ = unidades; break;
        }
        
        return transladar(unidadesX, unidadesY, unidadesZ, p);
    }
}
