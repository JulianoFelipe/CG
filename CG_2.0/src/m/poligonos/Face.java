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
public class Face extends CGObject{ //extends CGObject{
    //private List<Aresta> listaAresta;
    
    public Face (List<Vertice> lista){
        super(lista.size());
        
        for (int i=0; i<lista.size(); i++){
            Vertice v = lista.get(i);
            pointMatrix[0][i] = v.getX();
            pointMatrix[1][i] = v.getY();
            pointMatrix[2][i] = v.getZ();
            pointMatrix[3][i] = v.getW();
        }
    }
    
    //Não pode ser adicionado por já ter outro construtor como o mesmo nome
    /*public Face (List<Aresta> lista){
        super(lista.size()*2);
        throw new IllegalArgumentException("Arrumar implementação");
    }*/
    
    public Face (Face f){
        super(f.pointMatrix, f.ID);
    }
   
    @Override
    public String toString(){
        return "Face: ID=" + ID + "; Points=" + getNumberOfPoints() + ".";
    }
}
