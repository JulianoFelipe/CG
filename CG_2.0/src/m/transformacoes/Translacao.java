/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import m.Eixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class Translacao{
    
    private boolean lockChange = false;

    public Translacao() {
    }
    
    public Translacao(boolean lockChange) {
        this.lockChange = lockChange;
    }
    
    public void transladar(int unidadesX, int unidadesY, int unidadesZ, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            p.get(i).setAll( 
                (copy.getX() + unidadesX), 
                (copy.getY() + unidadesY),
                (copy.getZ() + unidadesZ)
            );
        }
        
        /*if (!lockChange)
            p.changedProperty().set(true);*/
    }
    
    public void transladar(Eixo axis, int unidades, CGObject p){
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
        
        transladar(unidadesX, unidadesY, unidadesZ, p);
        //p.changedProperty().set(true);
    }

    public boolean isLockChange() {
        return lockChange;
    }

    public void setLockChange(boolean lockChange) {
        this.lockChange = lockChange;
    }
}
