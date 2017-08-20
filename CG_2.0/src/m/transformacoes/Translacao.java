/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import java.util.List;
import m.Eixo;
import m.Ponto2D;
import m.poligonos.Poligono;

/**
 *
 * @author JFPS
 */
public class Translacao{
    /**
     * Translação com o mesmo número de unidades
     * para todos os eixos passados no parâmetro "axis".
     * 
     * @param axis Eixos para aplicar a translação.
     * @param unidades Unidades para transladar.
     * @param listaDePontos Lista de pontos para transladar.
     * @return lista De pontos com a translação aplicada.
     */
    public List<Ponto2D> transladar(Eixo axis, int unidades, List<Ponto2D> listaDePontos){
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
        
        return transladar(unidadesX, unidadesY, unidadesZ, listaDePontos);
    }
    
    /*
    public Poligono transladar(Eixo axis, int unidades, Poligono poligono){
        
    }
    
    public Poligono transladar(int unidadesX, int unidadesY, int unidadesZ, Poligono poligono){
        
    }*/
    
    /**
     * Translação aplicada sobre cada ponto na lista de pontos.
     * 
     * @param unidadesX unidades em X.
     * @param unidadesY unidades em Y.
     * @param unidadesZ unidades em Z.
     * @param listaDePontos Pontos para transladar.
     * @return pontos transladados.
     */
    public List<Ponto2D> transladar(int unidadesX, int unidadesY, int unidadesZ, List<Ponto2D> listaDePontos){
        ///FAÇA AQUI
        return null; //So coloquei para tirar o aviso de "sem return".
    }
}
